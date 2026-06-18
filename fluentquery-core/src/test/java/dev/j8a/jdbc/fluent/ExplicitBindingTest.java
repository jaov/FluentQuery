package dev.j8a.jdbc.fluent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExplicitBindingTest extends AbstractFluentQueryTest {

    @Test
    void testExplicitAndImplicitBindingMix() throws Exception {
        FluentQuery.insert("INSERT INTO users (name, email) VALUES (?, ?)")
                .bind(2, "john@example.com") // Explicit index 2
                .bind(1, "John")             // Explicit index 1
                .execute();

        String email = FluentQuery.forClass(String.class)
                .select("SELECT email FROM users WHERE name = ?")
                .bind(1, "John")
                .map(rs -> rs.getString(1))
                .one()
                .execute()
                .orElse(null);

        assertEquals("john@example.com", email);
    }
}
