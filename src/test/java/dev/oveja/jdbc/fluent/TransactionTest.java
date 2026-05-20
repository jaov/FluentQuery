package dev.oveja.jdbc.fluent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import dev.oveja.jdbc.fluent.api.Executor;
import org.junit.jupiter.api.Test;

import dev.oveja.jdbc.fluent.ConnectionSupplier;

public class TransactionTest extends AbstractFluentQueryTest {

    @Test
    void testCommit() throws Exception {
        FluentQuery.transaction(supplier, cs -> {
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Alice"))
                    .execute();
            
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Bob"))
                    .execute();
        });

        assertEquals(2, countUsers());
    }

    @Test
    void testRollback() throws SQLException {
        assertThrows(RuntimeException.class, () -> {
            FluentQuery.transaction(supplier, cs -> {
                FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                        .bind(ps -> ps.setString(1, "Alice"))
                        .execute();

                throw new RuntimeException("Force rollback");
            });
        });

        assertEquals(0, countUsers());
    }

    @Test
    void testManualCommit() throws Exception {
        FluentQuery.transaction(supplier, cs -> {
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Alice"))
                    .execute();
            
            cs.commit();

            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Bob"))
                    .execute();
        });

        assertEquals(2, countUsers());
    }

    @Test
    void testDoubleCommit() throws Exception {
        FluentQuery.transaction(supplier, cs -> {
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Alice"))
                    .execute();
            
            cs.commit(); // Manual commit 1

            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Bob"))
                    .execute();
            
            // Automatic commit will happen after this
        });

        assertEquals(2, countUsers());
    }

    @Test
    void testManualRollbackThenWork() throws Exception {
        FluentQuery.transaction(supplier, cs -> {
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Alice"))
                    .execute();
            
            cs.rollback(); // Alice is gone

            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind(ps -> ps.setString(1, "Bob"))
                    .execute();
            
            // Automatic commit will happen for Bob
        });

        assertEquals(1, countUsers());
        
        List<String> names = FluentQuery.forClass(supplier, String.class)
                .select("SELECT name FROM users")
                .noBind()
                .map(rs -> rs.getString(1))
                .execute();
        
        assertEquals("Bob", names.get(0));
    }

    @Test
    void testConnectionIdentity() throws Exception {
        Connection realConnection = DriverManager.getConnection(URL);
        ConnectionSupplier customSupplier = () -> realConnection;

        FluentQuery.transaction(customSupplier, cs -> {
            // Verify that the connection we get from the borrowed supplier
            // is EXACTLY the same instance we provided to the transaction block.
            assertSame(realConnection, cs.get(), "Borrowed supplier must return the exact same connection instance");
            
            // It should also return the same instance every time it's called
            assertSame(cs.get(), cs.get(), "Borrowed supplier must be consistent");
        });
        
        assertTrue(realConnection.isClosed(), "Connection MUST be closed by FluentQuery after the transaction block");
    }

    @Test
    void testTransactionResult() throws Exception {
        String name = FluentQuery.transactionResult(supplier, cs -> {
            FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                    .bind("Alice")
                    .execute();
            return "Alice";
        });

        assertEquals("Alice", name);
        assertEquals(1, countUsers());
    }

    @Test
    void testTransactionResultWithEntity() throws Exception {
        Executor<User> insertUser = cs -> {
            int id = FluentQuery.insertReturningIntId(cs, "INSERT INTO users (name, email) VALUES (?, ?)")
                    .bind("Bob")
                    .bind("bob@example.com")
                    .map(rs -> rs.getInt(1)) // Terminate binder with mapper
                    .fetchOne()
                    .orElseThrow();
            return new User(id, "Bob", "bob@example.com");
        };

        User user = FluentQuery.transactionResult(supplier, insertUser::execute);

        assertEquals("Bob", user.name);
        assertTrue(user.id > 0);
        assertEquals(1, countUsers());
    }

    @Test
    void testTransactionResultRollback() throws SQLException {
        assertThrows(RuntimeException.class, () -> {
            FluentQuery.transactionResult(supplier, cs -> {
                FluentQuery.insert(cs, "INSERT INTO users (name) VALUES (?)")
                        .bind("Alice")
                        .execute();
                throw new RuntimeException("Rollback");
            });
        });

        assertEquals(0, countUsers());
    }

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

    private int countUsers() throws SQLException {
        return FluentQuery.forClass(supplier, Integer.class)
                .select("SELECT COUNT(*) FROM users")
                .noBind()
                .map(rs -> rs.getInt(1))
                .fetchOne(supplier)
                .orElse(0);
    }
}
