package dev.j8a.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface CallableMapper<T> {
    T map(CallableStatement cs) throws SQLException;
}
