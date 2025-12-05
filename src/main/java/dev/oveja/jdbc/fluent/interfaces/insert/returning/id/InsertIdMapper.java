package dev.oveja.jdbc.fluent.interfaces.insert.returning.id;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InsertIdMapper<T> {
    List<T> executeReturningIds() throws SQLException;

    default Optional<T> executeReturningOneId() throws SQLException {
        List<T> results = executeReturningIds();
        int size = results.size();
        if (size > 1) { throw new RuntimeException("Query should return only one row instead of " + size); }
        return results.stream().findFirst();
    }
}
