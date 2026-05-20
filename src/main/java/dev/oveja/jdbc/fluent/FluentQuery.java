package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.api.DmlBinder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.api.GenericFlow;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.api.QueryBinder;
import dev.oveja.jdbc.fluent.internal.ConnectionSupplierLoader;
import dev.oveja.jdbc.fluent.internal.GenericPath;
import dev.oveja.jdbc.fluent.internal.InsertReturningIdPath;
import dev.oveja.jdbc.fluent.internal.DmlPath;
import dev.oveja.jdbc.fluent.transaction.TransactionStarter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public final class FluentQuery {

    private FluentQuery() {}

    public static <T> GenericFlow<T> forClass(Class<T> clazz) {
        return forClass(ConnectionSupplierLoader.load(), clazz);
    }

    public static <T> GenericFlow<T> forClass(ConnectionSupplier supplier, Class<T> clazz) {
        return new GenericPath<>(supplier, clazz);
    }

    public static <T> InsertReturningIdPath<T> insertReturningId(String sql) {
        return insertReturningId(ConnectionSupplierLoader.load(), sql);
    }

    public static <T> InsertReturningIdPath<T> insertReturningId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, sql);
    }

    public static DmlBinder update(String sql) {
        return update(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlBinder update(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static DmlBinder delete(String sql) {
        return delete(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlBinder delete(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static DmlBinder insert(String sql) {
        return insert(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlBinder insert(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static TransactionStarter transaction() {
        return transaction(ConnectionSupplierLoader.load());
    }

    public static TransactionStarter transaction(ConnectionSupplier supplier) {
        return new TransactionFlow<>(supplier);
    }

    public static void transaction(TransactionalVoidAction action) throws Exception {
        transaction().run(action).execute();
    }

    public static void transaction(ConnectionSupplier supplier, TransactionalVoidAction action) throws Exception {
        transaction(supplier).run(action).execute();
    }

    public static <R> R transactionResult(TransactionalAction<R> action) throws Exception {
        return transaction().returnUsing(action).execute();
    }

    public static <R> R transactionResult(ConnectionSupplier supplier, TransactionalAction<R> action) throws Exception {
        return transaction(supplier).returnUsing(action).execute();
    }

}
