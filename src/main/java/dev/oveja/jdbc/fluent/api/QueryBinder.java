package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.RowMapper;
import dev.oveja.jdbc.fluent.ResultMapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface QueryBinder<T, E extends Executor<?>> extends 
        FluentBinder<PreparedStatement, QueryBinder<T, E>>, 
        Mapper<ResultSet, T, E> {
    
    @Override
    default E map(ResultMapper<ResultSet, T> mapper) {
        return map((RowMapper<T>) mapper::map);
    }

    E map(RowMapper<T> mapper);
}
