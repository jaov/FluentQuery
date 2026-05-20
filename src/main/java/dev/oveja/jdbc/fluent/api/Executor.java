package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.internal.ConnectionSupplierLoader;
import java.sql.SQLException;

@FunctionalInterface
public interface Executor<R> {
    
    default R execute() throws SQLException {
        return execute(ConnectionSupplierLoader.load());
    }

    R execute(ConnectionSupplier supplier) throws SQLException;
}
