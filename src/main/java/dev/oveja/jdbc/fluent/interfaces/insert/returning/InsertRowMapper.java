package dev.oveja.jdbc.fluent.interfaces.insert.returning;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;

public interface InsertRowMapper<T> {
    InsertExecutor<T> map(RowMapper<T> mapper);
}
