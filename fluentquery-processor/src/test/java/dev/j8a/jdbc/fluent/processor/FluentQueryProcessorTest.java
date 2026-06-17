package dev.j8a.jdbc.fluent.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class FluentQueryProcessorTest {

    @Test
    void testImmutablePojoMapping() {
        JavaFileObject source = JavaFileObjects.forSourceLines("dev.j8a.jdbc.fluent.test.User",
                "package dev.j8a.jdbc.fluent.test;",
                "import dev.j8a.jdbc.fluent.processor.FluentQueryMapper;",
                "import java.time.LocalDateTime;",
                "",
                "@FluentQueryMapper",
                "public final class User {",
                "    private final int id;",
                "    private final String username;",
                "    private final LocalDateTime createdAt;",
                "",
                "    public User(int id, String username, LocalDateTime createdAt) {",
                "        this.id = id;",
                "        this.username = username;",
                "        this.createdAt = createdAt;",
                "    }",
                "}");

        Compilation compilation = javac()
                .withProcessors(new FluentQueryProcessor())
                .compile(source);

        assertThat(compilation).succeeded();
        assertThat(compilation).generatedSourceFile("dev.j8a.jdbc.fluent.test.UserRowMapper")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("ExpectedUserRowMapper.java"));
    }
    }
