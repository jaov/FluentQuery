package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.RowMapper;
import java.sql.PreparedStatement;

public interface QueryBinder<T, E extends Executor<?>> extends 
        FluentBinder<PreparedStatement, QueryBinder<T, E>> {

    E map(RowMapper<T> mapper);
}
