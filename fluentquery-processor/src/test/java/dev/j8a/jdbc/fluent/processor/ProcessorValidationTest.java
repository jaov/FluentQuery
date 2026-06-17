package dev.j8a.jdbc.fluent.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import javax.tools.JavaFileObject;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ProcessorValidationTest {

    @Test
    void testNonPublicClassFails() {
        JavaFileObject source = JavaFileObjects.forSourceLines("dev.j8a.jdbc.fluent.test.PrivateUser",
                "package dev.j8a.jdbc.fluent.test;",
                "import dev.j8a.jdbc.fluent.processor.FluentQueryMapper;",
                "",
                "@FluentQueryMapper",
                "final class PrivateUser {",
                "}");

        Compilation compilation = javac()
                .withProcessors(new FluentQueryProcessor())
                .compile(source);

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("Annotated class must be public");
    }

    @Test
    void testNoViableMappingStrategyFails() {
        JavaFileObject source = JavaFileObjects.forSourceLines("dev.j8a.jdbc.fluent.test.InvalidUser",
                "package dev.j8a.jdbc.fluent.test;",
                "import dev.j8a.jdbc.fluent.processor.FluentQueryMapper;",
                "",
                "@FluentQueryMapper",
                "public final class InvalidUser {",
                "    private String name;", // No setter or constructor to map this
                "}");

        Compilation compilation = javac()
                .withProcessors(new FluentQueryProcessor())
                .compile(source);

        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("No viable mapping strategy found");
    }
}
