package dev.oveja.jdbc.fluent.interfaces.insert.returning;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InsertExecutor<T> {
    List<T> executeReturning() throws SQLException;
    List<T> executeReturning(ConnectionSupplier supplier) throws SQLException;

    default ThrowingFunction<ConnectionSupplier, List<T>, SQLException> asFunction() {
        return this::executeReturning;
    }

    default Optional<T> executeReturningOne() throws SQLException {
        List<T> results = executeReturning();

        int size = results.size();

        if(size > 1) {
            throw new RuntimeException("Query should only return 1 row instead of " + size);
        }

        return results.stream().findFirst();

    }

    default Optional<T> executeReturningOne(ConnectionSupplier supplier) throws SQLException {
        List<T> results = executeReturning(supplier);

        int size = results.size();

        if(size > 1) {
            throw new RuntimeException("Query should only return 1 row instead of " + size);
        }

        return results.stream().findFirst();

    }
}
