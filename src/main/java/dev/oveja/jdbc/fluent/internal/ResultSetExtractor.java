package dev.oveja.jdbc.fluent.internal;

import java.sql.ResultSet;
import java.sql.SQLException;


import dev.oveja.jdbc.fluent.RowMapper;

@FunctionalInterface
public interface ResultSetExtractor<T, R> {
    R extract(ResultSet rs, RowMapper<T> mapper) throws SQLException;
}
