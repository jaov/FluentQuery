package dev.oveja.jdbc.fluent;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CrudTest extends AbstractFluentQueryTest {

    static class User {
        int id;
        String name;
        String email;

        User(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    @Test
    void testInsertAndSelect() throws Exception {
        FluentQuery.insert(supplier, "INSERT INTO users (name, email) VALUES (?, ?)")
                .bind(ps -> {
                    ps.setString(1, "John");
                    ps.setString(2, "john@example.com");
                })
                .execute();

        List<User> users = FluentQuery.forClass(supplier, User.class)
                .select("SELECT * FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "John"))
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .execute();

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).name);
        assertEquals("john@example.com", users.get(0).email);
    }

    @Test
    void testUpdate() throws Exception {
        FluentQuery.insert(supplier, "INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "Old Name"))
                .execute();

        int updated = FluentQuery.update(supplier, "UPDATE users SET name = ? WHERE name = ?")
                .bind(ps -> {
                    ps.setString(1, "New Name");
                    ps.setString(2, "Old Name");
                })
                .execute();

        assertEquals(1, updated);

        Optional<String> name = FluentQuery.forClass(supplier, String.class)
                .select("SELECT name FROM users")
                .noBind()
                .map(rs -> rs.getString(1))
                .fetchOne();

        assertTrue(name.isPresent());
        assertEquals("New Name", name.get());
    }

    @Test
    void testDelete() throws Exception {
        FluentQuery.insert(supplier, "INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "To Delete"))
                .execute();

        FluentQuery.delete(supplier, "DELETE FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "To Delete"))
                .execute();

        List<String> names = FluentQuery.forClass(supplier, String.class)
                .select("SELECT name FROM users")
                .noBind()
                .map(rs -> rs.getString(1))
                .execute();

        assertTrue(names.isEmpty());
    }

    @Test
    void testAsFunction() throws Exception {
        dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction<dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier, List<User>, SQLException> searchFunc =
                FluentQuery.forClass(supplier, User.class)
                        .select("SELECT * FROM users WHERE name = ?")
                        .bind(ps -> ps.setString(1, "Alice"))
                        .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                        .asFunction();

        // Setup
        FluentQuery.insert(supplier, "INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "Alice"))
                .execute();

        // Execute using function
        List<User> results = searchFunc.apply(supplier);
        assertEquals(1, results.size());
        assertEquals("Alice", results.get(0).name);
    }
}
