package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.RowMapper;

public interface Mapper<S, T, R> {
    R map(RowMapper<T> mapper);
}
