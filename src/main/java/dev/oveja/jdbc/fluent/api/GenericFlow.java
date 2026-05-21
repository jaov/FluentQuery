package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import java.sql.PreparedStatement;

public interface GenericFlow <T>{
    dev.oveja.jdbc.fluent.internal.SelectPath<T> select(String sql);

    dev.oveja.jdbc.fluent.internal.InsertReturningPath<T> insertReturning(String sql);

    dev.oveja.jdbc.fluent.internal.CallPath<T> call(String sql);
}
