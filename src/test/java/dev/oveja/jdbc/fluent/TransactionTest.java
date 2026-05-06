package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;
import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void testComposition() throws Exception {
        ThrowingConsumer<ConnectionSupplier, SQLException> insertAlice =
                FluentQuery.insert(supplier, "INSERT INTO users (name) VALUES (?)")
                        .bind(ps -> ps.setString(1, "Alice"))
                        .asConsumer();

        ThrowingFunction<ConnectionSupplier, List<String>, SQLException> selectNames =
                FluentQuery.forClass(supplier, String.class)
                        .select("SELECT name FROM users")
                        .noBind()
                        .map(rs -> rs.getString(1))
                        .asFunction();

        FluentQuery.transaction(supplier, cs -> {
            insertAlice.accept(cs);
            List<String> names = selectNames.apply(cs);
            assertEquals(1, names.size());
            assertEquals("Alice", names.get(0));
        });

        assertEquals(1, countUsers());
    }

    private int countUsers() throws SQLException {
        return FluentQuery.forClass(supplier, Integer.class)
                .select("SELECT COUNT(*) FROM users")
                .noBind()
                .map(rs -> rs.getInt(1))
                .asFetchOneFunction()
                .apply(supplier)
                .orElse(0);
    }
}
