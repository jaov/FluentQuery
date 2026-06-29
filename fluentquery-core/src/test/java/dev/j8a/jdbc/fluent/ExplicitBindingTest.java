package dev.j8a.jdbc.fluent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExplicitBindingTest extends AbstractFluentQueryTest {

    @Test
    void testExplicitAndImplicitBindingMix() throws Exception {
        FluentQuery.insert("INSERT INTO users (name, email) VALUES (?, ?)")
            .bind("John", "john@example.com")
            .log(System.out::println)
            .execute(supplier);

        String email = FluentQuery.forClass(String.class)
                .select("SELECT email FROM users WHERE name = ?")
                .bind("John")
                .map(rs -> rs.getString(1))
                .one()
                .execute(supplier)
                .orElse(null);

        assertEquals("john@example.com", email);
    }
}
