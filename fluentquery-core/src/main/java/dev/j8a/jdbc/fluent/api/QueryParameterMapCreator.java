package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;


import dev.j8a.jdbc.fluent.RowMapper;

public interface QueryParameterMapCreator<T, E extends QueryExecutor<?>> extends
    FluentParameterMapCreator<PreparedStatement, QueryParameterMapCreator<T, E>> {

    E map(RowMapper<T> mapper);
}
