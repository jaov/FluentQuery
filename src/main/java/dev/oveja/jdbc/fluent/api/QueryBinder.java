package dev.oveja.jdbc.fluent.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface QueryBinder<T, E extends Executor<?>> extends 
        FluentBinder<QueryBinder<T, E>>, 
        Mapper<ResultSet, T, E> {
}
