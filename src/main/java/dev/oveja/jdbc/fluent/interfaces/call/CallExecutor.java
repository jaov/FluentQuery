package dev.oveja.jdbc.fluent.interfaces.call;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

import java.sql.SQLException;

public interface CallExecutor<T> {
    T execute() throws SQLException;
    T execute(ConnectionSupplier supplier) throws SQLException;

    default ThrowingFunction<ConnectionSupplier, T, SQLException> asFunction() {
        return this::execute;
    }
}
