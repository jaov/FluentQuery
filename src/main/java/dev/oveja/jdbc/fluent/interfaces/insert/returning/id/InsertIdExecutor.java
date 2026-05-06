package dev.oveja.jdbc.fluent.interfaces.insert.returning.id;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InsertIdExecutor<T> {
    List<T> executeReturningIds() throws SQLException;
    List<T> executeReturningIds(ConnectionSupplier supplier) throws SQLException;

    default ThrowingFunction<ConnectionSupplier, List<T>, SQLException> asFunction() {
        return this::executeReturningIds;
    }

    default Optional<T> executeReturningOneId() throws SQLException {
        List<T> results = executeReturningIds();
        int size = results.size();
        if (size > 1) { throw new RuntimeException("Query should return only one row instead of " + size); }
        return results.stream().findFirst();
    }

    default Optional<T> executeReturningOneId(ConnectionSupplier supplier) throws SQLException {
        List<T> results = executeReturningIds(supplier);
        int size = results.size();
        if (size > 1) { throw new RuntimeException("Query should return only one row instead of " + size); }
        return results.stream().findFirst();
    }
}
