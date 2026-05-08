package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.api.GenericFlow;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.internal.ConnectionSupplierLoader;
import dev.oveja.jdbc.fluent.internal.GenericPath;
import dev.oveja.jdbc.fluent.internal.InsertReturningIdPath;
import dev.oveja.jdbc.fluent.internal.DmlPath;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class FluentQuery {

    private FluentQuery() {}

    public static <T> GenericFlow<T> forClass(Class<T> clazz) {
        return forClass(ConnectionSupplierLoader.load(), clazz);
    }

    public static <T> GenericFlow<T> forClass(ConnectionSupplier supplier, Class<T> clazz) {
        return new GenericPath<>(supplier, clazz);
    }

    public static Binder<PreparedStatement, ListExecutor<Integer>> insertReturningIntId(String sql) {
        return insertReturningIntId(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, ListExecutor<Integer>> insertReturningIntId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Integer.class, rs -> rs.getInt(1), sql);
    }

    public static Binder<PreparedStatement, ListExecutor<Long>> insertReturningLongId(String sql) {
        return insertReturningLongId(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, ListExecutor<Long>> insertReturningLongId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Long.class, rs -> rs.getLong(1), sql);
    }

    public static Binder<PreparedStatement, ListExecutor<String>> insertReturningStringId(String sql) {
        return insertReturningStringId(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, ListExecutor<String>> insertReturningStringId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, String.class, rs -> rs.getString(1), sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> update(String sql) {
        return update(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> update(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> delete(String sql) {
        return delete(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> delete(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> insert(String sql) {
        return insert(ConnectionSupplierLoader.load(), sql);
    }

    public static Binder<PreparedStatement, Executor<Integer>> insert(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static void transaction(ThrowingConsumer<BorrowedConnectionSupplier, Exception> action) throws Exception {
        transaction(ConnectionSupplierLoader.load(), action);
    }

    public static void transaction(ConnectionSupplier supplier, ThrowingConsumer<BorrowedConnectionSupplier, Exception> action) throws Exception {
        try (Connection con = supplier.get()) {
            con.setAutoCommit(false);
            try {
                action.accept(ConnectionSupplier.borrowed(con));
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

}
