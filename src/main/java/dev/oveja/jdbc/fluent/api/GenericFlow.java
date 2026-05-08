package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ConnectionSupplier;
import java.sql.PreparedStatement;
import java.util.Optional;

public interface GenericFlow <T>{
    Binder<java.sql.PreparedStatement, Mapper<java.sql.ResultSet, T, ListExecutor<T>>> select(String sql);

    Binder<java.sql.PreparedStatement, Mapper<java.sql.ResultSet, T, Executor<Optional<T>>>> selectOne(String sql);

    Binder<java.sql.PreparedStatement, Mapper<java.sql.ResultSet, T, ListExecutor<T>>> insertReturning(String sql);

    dev.oveja.jdbc.fluent.internal.CallPath<T> call(String sql);
    }
