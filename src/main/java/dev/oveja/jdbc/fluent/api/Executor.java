package dev.oveja.jdbc.fluent.api;

import java.sql.SQLException;


import dev.oveja.jdbc.fluent.ConnectionSupplier;

public interface Executor<R> {
    R execute() throws SQLException;
    R execute(ConnectionSupplier supplier) throws SQLException;
}
