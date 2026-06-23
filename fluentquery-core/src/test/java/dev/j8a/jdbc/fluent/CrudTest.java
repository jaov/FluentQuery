package dev.j8a.jdbc.fluent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


import dev.j8a.jdbc.fluent.api.QueryExecutor;
import org.junit.jupiter.api.Test;

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
        FluentQuery.insert("INSERT INTO users (name, email) VALUES (?, ?)")
                .bind(ps -> {
                    ps.setString(1, "John");
                    ps.setString(2, "john@example.com");
                })
                .execute(supplier);

        List<User> users = FluentQuery.forClass(User.class)
                .select("SELECT * FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "John"))
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .list()
                .execute(supplier);

        assertEquals(1, users.size());
        assertEquals("John", users.get(0).name);
        assertEquals("john@example.com", users.get(0).email);
    }

    @Test
    void testUpdate() throws Exception {
        FluentQuery.insert("INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "Old Name"))
                .execute(supplier);

        int updated = FluentQuery.update("UPDATE users SET name = ? WHERE name = ?")
                .bind(ps -> {
                    ps.setString(1, "New Name");
                    ps.setString(2, "Old Name");
                })
                .execute(supplier);

        assertEquals(1, updated);

        Optional<String> name = FluentQuery.forClass(String.class)
                .select("SELECT name FROM users")
                .noBind()
                .map(rs -> rs.getString(1))
                .one()
                .execute(supplier);

        assertTrue(name.isPresent());
        assertEquals("New Name", name.get());
    }

    @Test
    void testDelete() throws Exception {
        FluentQuery.insert("INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "To Delete"))
                .execute(supplier);

        FluentQuery.delete("DELETE FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "To Delete"))
                .execute(supplier);

        List<String> names = FluentQuery.forClass(String.class)
                .select("SELECT name FROM users")
                .noBind()
                .map(rs -> rs.getString(1))
                .list()
                .execute(supplier);

        assertTrue(names.isEmpty());
    }

    @Test
    void testSelectOne() throws Exception {
        FluentQuery.insert("INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "Alice"))
                .execute(supplier);

        Optional<User> user = FluentQuery.forClass(User.class)
                .select("SELECT * FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "Alice"))
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .one()
                .execute(supplier);

        assertTrue(user.isPresent());
        assertEquals("Alice", user.get().name);

        Optional<User> none = FluentQuery.forClass(User.class)
                .select("SELECT * FROM users WHERE name = ?")
                .bind(ps -> ps.setString(1, "Bob"))
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .one()
                .execute(supplier);

        assertFalse(none.isPresent());
    }

    @Test
    void testInsertReturningEntity() throws Exception {
        setupH2Returning();
        
        User user = FluentQuery.forClass(User.class)
                .insertReturning("CALL INSERT_RETURNING('Alice', 'alice@example.com')")
                .noBind()
                .map(rs -> new User(rs.getInt(1), "Alice", "alice@example.com"))
                .list()
                .execute(supplier)
                .get(0);

        assertEquals("Alice", user.name);
        assertEquals("alice@example.com", user.email);
        assertTrue(user.id > 0);
    }

    @Test
    void testAsFunction() throws Exception {
        QueryExecutor<List<User>> searchFunc =
                FluentQuery.forClass(User.class)
                        .select("SELECT * FROM users WHERE name = ?")
                        .bind(ps -> ps.setString(1, "Alice"))
                        .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                        .list();

        // Setup
        FluentQuery.insert("INSERT INTO users (name) VALUES (?)")
                .bind(ps -> ps.setString(1, "Alice"))
                .execute();

        // Execute using function
        List<User> results = searchFunc.execute(supplier);
        assertEquals(1, results.size());
        assertEquals("Alice", results.get(0).name);
    }

    @Test
    void testTableReturningFunction() throws Exception {
        // Setup H2 ALIAS for a table-returning function
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("CREATE ALIAS GET_USERS_FUNC FOR \"dev.j8a.jdbc.fluent.CrudTest.getUsersFunc\"");
        }

        List<User> users = FluentQuery.forClass(User.class)
                .select("SELECT * FROM GET_USERS_FUNC()")
                .noBind()
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .list()
                .execute(supplier);

        assertEquals(2, users.size());
    }

    @Test
    void testLogging() throws Exception {
        java.util.concurrent.atomic.AtomicReference<dev.j8a.jdbc.fluent.api.QueryContext> loggedCtx = new java.util.concurrent.atomic.AtomicReference<>();

        FluentQuery.insert("INSERT INTO users (name, email) VALUES (?, ?)")
                .bind(1, "LoggerUser")
                .bind(2, "logger@example.com")
                .log(loggedCtx::set)
                .execute(supplier);

        assertNotNull(loggedCtx.get());
        assertEquals("INSERT INTO users (name, email) VALUES (?, ?)", loggedCtx.get().getSql());
        assertEquals("LoggerUser", loggedCtx.get().getBoundParameters().get(1));
        assertEquals("logger@example.com", loggedCtx.get().getBoundParameters().get(2));
        assertNotNull(loggedCtx.get().getStatementRepresentation());
        assertTrue(loggedCtx.get().getExecutionTimeNanos() > 0);

        loggedCtx.set(null);

        List<User> users = FluentQuery.forClass(User.class)
                .select("SELECT * FROM users WHERE name = ?")
                .bind(1, "LoggerUser")
                .map(rs -> new User(rs.getInt("id"), rs.getString("name"), rs.getString("email")))
                .list()
                .log(loggedCtx::set)
                .execute(supplier);

        assertNotNull(loggedCtx.get());
        assertEquals("SELECT * FROM users WHERE name = ?", loggedCtx.get().getSql());
        assertEquals("LoggerUser", loggedCtx.get().getBoundParameters().get(1));
        assertNotNull(loggedCtx.get().getStatementRepresentation());
        assertTrue(loggedCtx.get().getExecutionTimeNanos() > 0);
    }

    public static ResultSet getUsersFunc(Connection con) throws SQLException {
        return con.createStatement().executeQuery("SELECT 1 AS id, 'Alice' AS name, 'alice@example.com' AS email UNION ALL SELECT 2, 'Bob', 'bob@example.com'");
    }
}
