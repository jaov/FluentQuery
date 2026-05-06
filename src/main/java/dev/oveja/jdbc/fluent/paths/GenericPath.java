package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.GenericFlow;
import dev.oveja.jdbc.fluent.interfaces.call.CallInParam;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertStatementBinder;
import dev.oveja.jdbc.fluent.interfaces.select.SelectBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

public class GenericPath <T> implements GenericFlow<T>{

    private final ConnectionSupplier supplier;
    private final Class<T> clazz;

    public GenericPath(ConnectionSupplier supplier, Class<T> clazz) {
        this.supplier = supplier;
        this.clazz = clazz;
    }


    @Override
    public SelectBinder<T> select(String sql) {
        return new SelectPath<>(this.supplier, this.clazz, sql);
    }

    @Override
    public InsertStatementBinder<T> insertReturning(String sql) {
        return new InsertReturningPath<>(this.supplier, this.clazz, sql);
    }

    @Override
    public CallInParam<T> call(String sql) { return  new CallPath<>(this.supplier, this.clazz,sql);}


}
