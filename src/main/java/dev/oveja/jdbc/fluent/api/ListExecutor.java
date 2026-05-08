package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.ThrowingFunction;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ListExecutor<T> extends Executor<List<T>> {
    default Optional<T> fetchOne() throws SQLException {
        List<T> list = execute();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    default Optional<T> fetchOne(ConnectionSupplier supplier) throws SQLException {
        List<T> list = execute(supplier);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    default ThrowingFunction<ConnectionSupplier, Optional<T>, SQLException> asFetchOneFunction() {
        return this::fetchOne;
    }
}
