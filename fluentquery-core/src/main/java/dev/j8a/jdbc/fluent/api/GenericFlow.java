package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;

import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.internal.CallPath;
import dev.j8a.jdbc.fluent.internal.InsertReturningPath;
import dev.j8a.jdbc.fluent.internal.SelectPath;

public interface GenericFlow<T> {
    SelectPath<T> select(String sql);

    InsertReturningPath<T> insertReturning(String sql);

    CallPath<T> call(String sql);
}
