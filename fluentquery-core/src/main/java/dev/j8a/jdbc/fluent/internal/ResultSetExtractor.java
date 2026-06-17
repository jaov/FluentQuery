package dev.j8a.jdbc.fluent.internal;

import java.sql.ResultSet;
import java.sql.SQLException;


import dev.j8a.jdbc.fluent.RowMapper;

@FunctionalInterface
public interface ResultSetExtractor<T, R> {
    R extract(ResultSet rs, RowMapper<T> mapper) throws SQLException;
}
