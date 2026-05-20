package dev.oveja.jdbc.fluent;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> extends ResultMapper<java.sql.ResultSet, T> {
    @Override
    T map(java.sql.ResultSet rs) throws SQLException;
}
