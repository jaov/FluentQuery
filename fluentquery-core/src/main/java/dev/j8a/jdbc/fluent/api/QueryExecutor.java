package dev.j8a.jdbc.fluent.api;

import java.sql.SQLException;
import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.ConnectionSupplier;

public interface QueryExecutor<R> {
    R execute() throws SQLException;
    R execute(ConnectionSupplier supplier) throws SQLException;
    QueryExecutor<R> log(Consumer<QueryContext> logConsumer);
}

