package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.api.DmlBinder;
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

public final class FluentQuery {

    private FluentQuery() {}

    public static <T> GenericFlow<T> forClass(Class<T> clazz) {
        return forClass(ConnectionSupplierLoader.load(), clazz);
    }

    public static <T> GenericFlow<T> forClass(ConnectionSupplier supplier, Class<T> clazz) {
        return new GenericPath<>(supplier, clazz);
    }

    public static QueryBinder<Integer, ListExecutor<Integer>> insertReturningIntId(String sql) {
        return insertReturningIntId(ConnectionSupplierLoader.load(), sql);
    }

    public static QueryBinder<Integer, ListExecutor<Integer>> insertReturningIntId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Integer.class, rs -> rs.getInt(1), sql);
    }

    public static QueryBinder<Long, ListExecutor<Long>> insertReturningLongId(String sql) {
        return insertReturningLongId(ConnectionSupplierLoader.load(), sql);
    }

    public static QueryBinder<Long, ListExecutor<Long>> insertReturningLongId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Long.class, rs -> rs.getLong(1), sql);
    }

    public static QueryBinder<String, ListExecutor<String>> insertReturningStringId(String sql) {
        return insertReturningStringId(ConnectionSupplierLoader.load(), sql);
    }

    public static QueryBinder<String, ListExecutor<String>> insertReturningStringId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, String.class, rs -> rs.getString(1), sql);
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
