package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetExtractor<T, R> {
    R extract(ResultSet rs, RowMapper<T> mapper) throws SQLException;
}
