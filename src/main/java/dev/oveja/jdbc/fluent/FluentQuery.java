package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.interfaces.GenericFlow;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.id.InsertIdBinder;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;
import dev.oveja.jdbc.fluent.paths.GenericPath;
import dev.oveja.jdbc.fluent.paths.InsertReturningIdPath;
import dev.oveja.jdbc.fluent.paths.DmlPath;

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

    public static InsertIdBinder<Integer> insertReturningIntId(String sql) {
        return insertReturningIntId(ConnectionSupplierLoader.load(), sql);
    }

    public static InsertIdBinder<Integer> insertReturningIntId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Integer.class, rs -> rs.getInt(1), sql);
    }

    public static InsertIdBinder<Long> insertReturningLongId(String sql) {
        return insertReturningLongId(ConnectionSupplierLoader.load(), sql);
    }

    public static InsertIdBinder<Long> insertReturningLongId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, Long.class, rs -> rs.getLong(1), sql);
    }

    public static InsertIdBinder<String> insertReturningStringId(String sql) {
        return insertReturningStringId(ConnectionSupplierLoader.load(), sql);
    }

    public static InsertIdBinder<String> insertReturningStringId(ConnectionSupplier supplier, String sql) {
        return new InsertReturningIdPath<>(supplier, String.class, rs -> rs.getString(1), sql);
    }

    public static DmlStatementBinder update(String sql) {
        return update(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlStatementBinder update(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static DmlStatementBinder delete(String sql) {
        return delete(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlStatementBinder delete(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static DmlStatementBinder insert(String sql) {
        return insert(ConnectionSupplierLoader.load(), sql);
    }

    public static DmlStatementBinder insert(ConnectionSupplier supplier, String sql) {
        return new DmlPath(supplier, sql);
    }

    public static void transaction(ThrowingConsumer<ConnectionSupplier, Exception> action) throws Exception {
        transaction(ConnectionSupplierLoader.load(), action);
    }

    public static void transaction(ConnectionSupplier supplier, ThrowingConsumer<ConnectionSupplier, Exception> action) throws Exception {
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
