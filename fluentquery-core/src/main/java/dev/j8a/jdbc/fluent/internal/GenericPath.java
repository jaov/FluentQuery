package dev.j8a.jdbc.fluent.internal;

import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.api.*;

public class GenericPath<T> implements GenericFlow<T> {

    private final Class<T> clazz;

    public GenericPath(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public SelectPath<T> select(String sql) {
        return new SelectPath<>(sql);
    }

    @Override
    public InsertReturningPath<T> insertReturning(String sql) {
        return new InsertReturningPath<>(sql, false);
    }

    @Override
    public CallPath<T> call(String sql) { 
        return new CallPath<>(this.clazz, sql);
    }

}
