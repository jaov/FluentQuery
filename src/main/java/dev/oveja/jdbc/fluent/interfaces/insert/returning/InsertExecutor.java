package dev.oveja.jdbc.fluent.interfaces.insert.returning;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InsertExecutor<T> {
    List<T> executeReturning() throws SQLException;

    default Optional<T> executeReturningOne() throws SQLException {
        List<T> results = executeReturning();

        int size = results.size();

        if(size > 1) {
            throw new RuntimeException("Query should only return 1 row instead of " + size);
        }

        return results.stream().findFirst();

    }
}
