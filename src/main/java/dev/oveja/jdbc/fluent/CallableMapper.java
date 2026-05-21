package dev.oveja.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface CallableMapper<T> {
    T map(java.sql.CallableStatement cs) throws SQLException;
}
