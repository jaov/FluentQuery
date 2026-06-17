package dev.j8a.jdbc.fluent.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import javax.tools.JavaFileObject;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class ImmutablesProcessorTest {

    @Test
    void testImmutablesStylePojoMapping() {
        JavaFileObject source = JavaFileObjects.forSourceLines("dev.j8a.jdbc.fluent.test.ImmutableUser",
                "package dev.j8a.jdbc.fluent.test;",
                "import dev.j8a.jdbc.fluent.processor.FluentQueryMapper;",
                "import org.immutables.value.Value;",
                "",
                "@FluentQueryMapper",
                "@Value.Immutable",
                "public abstract class ImmutableUser {",
                "    public abstract int id();",
                "    public abstract String username();",
                "}");

        Compilation compilation = javac()
                .withProcessors(new FluentQueryProcessor(), new org.immutables.value.processor.Processor())
                .compile(source);

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("dev.j8a.jdbc.fluent.test.ImmutableUserRowMapper")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("ExpectedImmutableUserRowMapper.java"));
    }
}
