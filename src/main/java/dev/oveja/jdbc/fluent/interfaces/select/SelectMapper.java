package dev.oveja.jdbc.fluent.interfaces.select;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;

public interface SelectMapper<T> {
    SelectExecute<T> map(RowMapper<T> mapper);
}
