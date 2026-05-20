package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import java.sql.SQLException;

public interface Executor<R> {
    R execute() throws SQLException;
    R execute(ConnectionSupplier supplier) throws SQLException;
}
