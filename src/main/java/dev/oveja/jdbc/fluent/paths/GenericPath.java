package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.GenericFlow;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertStatementBinder;
import dev.oveja.jdbc.fluent.interfaces.select.SelectBinder;

public class GenericPath <T> implements GenericFlow<T>{

    private final Class<T> clazz;

    public GenericPath(Class<T> clazz) {
        this.clazz = clazz;
    }


    @Override
    public SelectBinder<T> select(String sql) {
        return new SelectPath<>(this.clazz, sql);
    }

    @Override
    public InsertStatementBinder<T> insertReturning(String sql) {
        return new InsertReturningPath<>(this.clazz, sql);
    }


}
