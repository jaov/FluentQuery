package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.*;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class GenericPath <T> implements GenericFlow<T>{

    private final ConnectionSupplier supplier;
    private final Class<T> clazz;

    public GenericPath(ConnectionSupplier supplier, Class<T> clazz) {
        this.supplier = supplier;
        this.clazz = clazz;
    }


    @Override
    public QueryBinder<T, ListExecutor<T>> select(String sql) {
        return SelectPath.asList(this.supplier, sql);
    }

    @Override
    public QueryBinder<T, Executor<Optional<T>>> selectOne(String sql) {
        return SelectPath.asSingle(this.supplier, sql);
    }

    @Override
    public InsertReturningPath<T> insertReturning(String sql) {
        return new InsertReturningPath<>(this.supplier, sql, false);
    }

    @Override
    public CallPath<T> call(String sql) { 
        return new CallPath<>(this.supplier, this.clazz, sql);
    }



}
