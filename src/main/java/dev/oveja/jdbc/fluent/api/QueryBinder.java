package dev.oveja.jdbc.fluent.api;

import java.sql.PreparedStatement;


import dev.oveja.jdbc.fluent.RowMapper;

public interface QueryBinder<T, E extends Executor<?>> extends 
        FluentBinder<PreparedStatement, QueryBinder<T, E>> {

    E map(RowMapper<T> mapper);
}
