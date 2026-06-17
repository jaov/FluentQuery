package dev.j8a.jdbc.fluent.internal;

import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.api.*;

public class GenericPath<T> implements GenericFlow<T> {

    private final ConnectionSupplier supplier;
    private final Class<T> clazz;

    public GenericPath(ConnectionSupplier supplier, Class<T> clazz) {
        this.supplier = supplier;
        this.clazz = clazz;
    }

    @Override
    public SelectPath<T> select(String sql) {
        return new SelectPath<>(this.supplier, sql);
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
