package dev.oveja.jdbc.fluent.interfaces.call;

import java.sql.SQLException;

public interface CallExecutor<T> {
    T execute() throws SQLException;
}
