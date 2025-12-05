package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.interfaces.GenericFlow;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.id.InsertIdBinder;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementBinder;
import dev.oveja.jdbc.fluent.paths.GenericPath;
import dev.oveja.jdbc.fluent.paths.InsertReturningInsertInsertIdPath;
import dev.oveja.jdbc.fluent.paths.DmlPath;

public final class FluentQuery {

    private FluentQuery() {}

    public static <T> GenericFlow<T> forClass(Class<T> clazz) {
        return new GenericPath<>(clazz);
    }

    public static InsertIdBinder<Integer> insertReturningIntId(String sql) {
        return new InsertReturningInsertInsertIdPath<>(Integer.class, rs -> rs.getInt(1), sql);
    }

    public static InsertIdBinder<Long> insertReturningLongId(String sql) {
        return new InsertReturningInsertInsertIdPath<>(Long.class, rs -> rs.getLong(1), sql);
    }

    public static InsertIdBinder<String> insertReturningStringId(String sql) {
        return new InsertReturningInsertInsertIdPath<>(String.class, rs -> rs.getString(1), sql);
    }

    public static DmlStatementBinder update(String sql) {
        return new DmlPath(sql);
    }

    public static DmlStatementBinder delete(String sql) {
        return new DmlPath(sql);
    }

    public static DmlStatementBinder insert(String sql) {
        return new DmlPath(sql);
    }

}
