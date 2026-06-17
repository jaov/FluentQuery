package dev.j8a.jdbc.fluent.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import javax.tools.JavaFileObject;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class AutoValueProcessorTest {

    @Test
    void testAutoValueStylePojoMapping() {
        JavaFileObject source = JavaFileObjects.forSourceLines("dev.j8a.jdbc.fluent.test.AutoValueUser",
                "package dev.j8a.jdbc.fluent.test;",
                "import com.google.auto.value.AutoValue;",
                "import dev.j8a.jdbc.fluent.processor.FluentQueryMapper;",
                "",
                "@FluentQueryMapper",
                "@AutoValue",
                "public abstract class AutoValueUser {",
                "    public abstract int id();",
                "    public abstract String username();",
                "",
                "    public static AutoValueUser create(int id, String username) {",
                "        return null; // Mock implementation",
                "    }",
                "}");

        Compilation compilation = javac()
                .withProcessors(new FluentQueryProcessor())
                .compile(source);

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("dev.j8a.jdbc.fluent.test.AutoValueUserRowMapper")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("ExpectedAutoValueUserRowMapper.java"));
    }
}
