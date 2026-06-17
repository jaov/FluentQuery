package dev.j8a.jdbc.fluent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import dev.j8a.jdbc.fluent.api.*;
import dev.j8a.jdbc.fluent.internal.ConnectionSupplierLoader;
import dev.j8a.jdbc.fluent.internal.DmlPath;
import dev.j8a.jdbc.fluent.internal.GenericPath;
import dev.j8a.jdbc.fluent.internal.InsertReturningPath;
import dev.j8a.jdbc.fluent.internal.CallPath;
import dev.j8a.jdbc.fluent.transaction.TransactionStarter;

public final class FluentQuery {

    private FluentQuery() {}

    public static <T> GenericFlow<T> forClass(Class<T> clazz) {
        return new GenericPath<>(clazz);
    }

    public static <T> GenericFlow<T> forClass(ConnectionSupplier supplier, Class<T> clazz) {
        return new GenericPath<>(clazz);
    }

    public static <T> InsertReturningPath<T> insertReturningId(String sql) {
        return new InsertReturningPath<>(sql, true);
    }

    public static <T> InsertReturningPath<T> insertReturningId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningPath<>(sql, true);
    }

    public static DmlBinder update(String sql) {
        return new DmlPath(sql);
    }

    public static DmlBinder update(ConnectionSupplier supplier, String sql) {
        return new DmlPath(sql); // Supplier ignored for stateless path
    }

    public static DmlBinder delete(String sql) {
        return new DmlPath(sql);
    }

    public static DmlBinder delete(ConnectionSupplier supplier, String sql) {
        return new DmlPath(sql); // Supplier ignored for stateless path
    }

    public static DmlBinder insert(String sql) {
        return new DmlPath(sql);
    }

    public static DmlBinder insert(ConnectionSupplier supplier, String sql) {
        return new DmlPath(sql); // Supplier ignored for stateless path
    }

    public static <T> CallPath<T> call(Class<T> clazz, String sql) {
        return new CallPath<>(clazz, sql);
    }

    public static <T> CallPath<T> call(ConnectionSupplier supplier, Class<T> clazz, String sql) {
        return new CallPath<>(clazz, sql); // Supplier ignored for stateless path
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
