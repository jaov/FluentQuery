package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ListExecutor<T> extends Executor<List<T>> {
    default Optional<T> fetchOne() throws SQLException {
        return execute().stream().findFirst();
    }

    default Optional<T> fetchOne(ConnectionSupplier supplier) throws SQLException {
        return execute(supplier).stream().findFirst();
    }
}
