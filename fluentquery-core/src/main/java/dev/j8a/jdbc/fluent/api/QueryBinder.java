package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;


import dev.j8a.jdbc.fluent.RowMapper;

public interface QueryBinder<T, E extends QueryExecutor<?>> extends 
        FluentBinder<PreparedStatement, QueryBinder<T, E>> {

    E map(RowMapper<T> mapper);
}
