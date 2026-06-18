package dev.j8a.jdbc.fluent.api;

import java.sql.SQLException;


import dev.j8a.jdbc.fluent.ConnectionSupplier;

public interface QueryExecutor<R> {
    R execute() throws SQLException;
    R execute(ConnectionSupplier supplier) throws SQLException;
}
