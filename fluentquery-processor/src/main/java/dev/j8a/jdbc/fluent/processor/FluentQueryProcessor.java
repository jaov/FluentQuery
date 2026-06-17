package dev.j8a.jdbc.fluent.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import dev.j8a.jdbc.fluent.RowMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("dev.j8a.jdbc.fluent.processor.FluentQueryMapper")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FluentQueryProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(FluentQueryMapper.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@FluentQueryMapper can only be applied to classes", element);
                continue;
            }
            generateRowMapper((TypeElement) element);
        }
        return true;
    }

    private boolean hasAnnotation(TypeElement classElement, String annotationName) {
        return classElement.getAnnotationMirrors().stream()
                .anyMatch(a -> a.getAnnotationType().toString().equals(annotationName));
    }

    private void generateRowMapper(TypeElement classElement) {
        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Annotated class must be public", classElement);
            return;
        }

        String packageName = processingEnv.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
        String className = classElement.getSimpleName().toString();
        String mapperName = className + "RowMapper";

        // Handle abstract classes (AutoValue/Immutables)
        String implementationName = className;
        if (classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            if (hasAnnotation(classElement, "com.google.auto.value.AutoValue")) {
                implementationName = "AutoValue_" + className;
            } else if (hasAnnotation(classElement, "org.immutables.value.Value.Immutable")) {
                implementationName = "Immutable" + className;
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Abstract class must be annotated with @AutoValue or @Value.Immutable", classElement);
                return;
            }
        }

        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(ClassName.get(classElement))
                .addParameter(ResultSet.class, "rs")
                .addException(SQLException.class);

        // Analysis: Look for all-args constructor first, otherwise fall back to setters
        // For abstract classes, we assume constructor exists with matching parameters
        ExecutableElement constructor = findAllArgsConstructor(classElement, implementationName);
        boolean hasSetters = hasValidSetters(classElement);

        if (constructor != null || classElement.getModifiers().contains(Modifier.ABSTRACT)) {
            generateConstructorMapping(mapMethod, classElement, implementationName, constructor);
        } else if (hasSetters) {
            generateSetterMapping(mapMethod, classElement, implementationName);
        } else {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, 
                "No viable mapping strategy found. Class must have an all-args constructor or a default constructor with public setters.", classElement);
            return;
        }

        TypeSpec mapperClass = TypeSpec.classBuilder(mapperName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(RowMapper.class), ClassName.get(classElement)))
                .addMethod(mapMethod.build())
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, mapperClass).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to generate RowMapper: " + e.getMessage(), classElement);
        }
    }

    private boolean hasValidSetters(TypeElement classElement) {
        // Does it have a default constructor?
        boolean hasDefaultConstructor = classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.CONSTRUCTOR)
                .map(e -> (ExecutableElement) e)
                .anyMatch(e -> e.getParameters().isEmpty());
        
        if (!hasDefaultConstructor) return false;

        // Does it have a setter for every field?
        List<VariableElement> fields = getFields(classElement);
        for (VariableElement field : fields) {
            String fieldName = field.getSimpleName().toString();
            String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            boolean hasSetter = classElement.getEnclosedElements().stream()
                    .filter(e -> e.getKind() == ElementKind.METHOD)
                    .map(e -> (ExecutableElement) e)
                    .anyMatch(m -> m.getSimpleName().toString().equals(setterName) && m.getParameters().size() == 1);
            if (!hasSetter) return false;
        }

        return true;
    }

    private ExecutableElement findAllArgsConstructor(TypeElement classElement, String implementationName) {
        String packageName = processingEnv.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
        // If abstract, we need to look for the generated implementation class
        TypeElement implementationElement = classElement.getModifiers().contains(Modifier.ABSTRACT) 
            ? processingEnv.getElementUtils().getTypeElement(packageName + "." + implementationName)
            : classElement;

        if (implementationElement == null) return null;

        List<VariableElement> fields = getFields(classElement); // Use fields from the abstract class
        for (Element enclosed : implementationElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) enclosed;
                if (constructor.getParameters().size() == fields.size()) {
                    return constructor; 
                }
            }
        }
        return null;
    }

    private List<Element> getProperties(TypeElement classElement) {
        if (hasAnnotation(classElement, "com.google.auto.value.AutoValue") ||
            hasAnnotation(classElement, "org.immutables.value.Value.Immutable")) {
            return classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD && e.getModifiers().contains(Modifier.ABSTRACT))
                .collect(Collectors.toList());
        }
        return new ArrayList<>(getFields(classElement));
    }

    private void generateConstructorMapping(MethodSpec.Builder builder, TypeElement classElement, String implementationName, ExecutableElement constructor) {
        String packageName = processingEnv.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
        ClassName implementationClass = ClassName.get(packageName, implementationName);

        // Fallback to properties if constructor is null (e.g. for simple POJOs)
        List<? extends Element> parameters = (constructor != null) 
            ? (List<? extends Element>) constructor.getParameters() 
            : getProperties(classElement);
        
        // Handle AutoValue/Immutables instantiation differently
        if (hasAnnotation(classElement, "com.google.auto.value.AutoValue")) {
            // AutoValue: Assume static factory create(...)
            List<Object> args = new ArrayList<>();
            StringBuilder format = new StringBuilder("return $T.create(");
            args.add(ClassName.get(classElement));
            for (int i = 0; i < parameters.size(); i++) {
                Element param = parameters.get(i);
                // Extract name and type regardless of element type
                String name = param.getSimpleName().toString();
                javax.lang.model.type.TypeMirror type = (param instanceof ExecutableElement) 
                    ? ((ExecutableElement) param).getReturnType() 
                    : param.asType();
                String columnName = getColumnName(param);
                String typeName = type.toString();
                String objName = name + "Obj";
                String arg = String.format("rs.getObject(\"%s\", %s.class)", columnName, typeName);
                builder.addStatement("Object $L = $L", objName, arg);
                builder.beginControlFlow("if ($L == null && rs.wasNull())", objName);
                builder.addStatement("throw new $T(\"Column '$L' is null but parameter '$L' is not nullable\")", SQLException.class, columnName, name);
                builder.endControlFlow();
                format.append(i > 0 ? ", " : "").append("($L) $L");
                args.add(typeName);
                args.add(objName);
            }
            format.append(")");
            builder.addStatement(format.toString(), args.toArray());
        } else if (hasAnnotation(classElement, "org.immutables.value.Value.Immutable")) {
            // Immutables: Use Builder
            // Naming convention for Immutables is Immutable + ClassName.
            // If the class is named ImmutableUser, the generated class is ImmutableImmutableUser.
            ClassName immutableImplementation = ClassName.get(packageName, "Immutable" + classElement.getSimpleName().toString());
            ClassName builderClass = immutableImplementation.nestedClass("Builder");
            builder.addStatement("$T builder = $T.builder()", builderClass, immutableImplementation);
            for (Element param : parameters) {
                String columnName = getColumnName(param);
                String paramName = param.getSimpleName().toString();
                javax.lang.model.type.TypeMirror type = (param instanceof ExecutableElement) 
                    ? ((ExecutableElement) param).getReturnType() 
                    : param.asType();
                // Immutables builder uses setter method named as the attribute
                builder.addStatement("builder.$L(rs.getObject(\"$L\", $T.class))", paramName, columnName, type);
            }
            builder.addStatement("return builder.build()");
        } else {
            // Default POJO: Assume all-args constructor
            List<Object> args = new ArrayList<>();
            StringBuilder format = new StringBuilder("return new $T(");
            args.add(implementationClass);
            for (int i = 0; i < parameters.size(); i++) {
                Element param = parameters.get(i);
                String name = param.getSimpleName().toString();
                String columnName = getColumnName(param);
                String typeName = param.asType().toString();
                String objName = name + "Obj";
                String arg = String.format("rs.getObject(\"%s\", %s.class)", columnName, typeName);
                builder.addStatement("Object $L = $L", objName, arg);
                builder.beginControlFlow("if ($L == null && rs.wasNull())", objName);
                builder.addStatement("throw new $T(\"Column '$L' is null but parameter '$L' is not nullable\")", SQLException.class, columnName, name);
                builder.endControlFlow();
                format.append(i > 0 ? ", " : "").append("($L) $L");
                args.add(typeName);
                args.add(objName);
            }
            format.append(")");
            builder.addStatement(format.toString(), args.toArray());
        }
    }

    private void generateSetterMapping(MethodSpec.Builder builder, TypeElement classElement, String implementationName) {
        String packageName = processingEnv.getElementUtils().getPackageOf(classElement).getQualifiedName().toString();
        ClassName implementationClass = ClassName.get(packageName, implementationName);
        builder.addStatement("$T instance = new $T()", implementationClass, implementationClass);
        for (VariableElement field : getFields(classElement)) {
            String columnName = getColumnName(field);
            String fieldName = field.getSimpleName().toString();
            String setter = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            
            builder.addStatement("Object $L = rs.getObject(\"$L\", $T.class)", fieldName + "Obj", columnName, field.asType());
            builder.beginControlFlow("if ($L != null)", fieldName + "Obj");
            builder.addStatement("instance." + setter + "(($T) $L)", field.asType(), fieldName + "Obj");
            builder.endControlFlow();
        }
        builder.addStatement("return instance");
    }

    private List<VariableElement> getFields(TypeElement classElement) {
        return classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(e -> (VariableElement) e)
                .collect(Collectors.toList());
    }

    private String getColumnName(Element element) {
        if (element == null) return "unknown";
        String name = element.getSimpleName().toString();
        // For methods (AutoValue/Immutables), strip potential "get" prefix if not direct property name
        if (element.getKind() == ElementKind.METHOD && name.startsWith("get") && name.length() > 3) {
            name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
        }
        // Logic for camelCase to snake_case + @Column annotation lookup
        // Simplified: just return name for now
        return name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
