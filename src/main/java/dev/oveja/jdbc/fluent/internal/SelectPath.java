package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.api.Mapper;
import dev.oveja.jdbc.fluent.ThrowingBiFunction;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SelectPath<T, R, E extends Executor<R>> implements 
        Binder<PreparedStatement, Mapper<ResultSet, T, E>>, 
        Mapper<ResultSet, T, E>, 
        Executor<R> {

    protected final ConnectionSupplier supplier;
    protected final String sql;
    protected ThrowingConsumer<PreparedStatement, SQLException> binder = ps -> {};
    protected RowMapper<T> mapper;
    protected final ThrowingBiFunction<ResultSet, RowMapper<T>, R, SQLException> extractor;

    protected SelectPath(ConnectionSupplier supplier, String sql, ThrowingBiFunction<ResultSet, RowMapper<T>, R, SQLException> extractor) {
        this.supplier = supplier;
        this.sql = sql;
        this.extractor = extractor;
    }

    @Override
    public Mapper<ResultSet, T, E> bind(ThrowingConsumer<PreparedStatement, SQLException> binder) {
        this.binder = binder;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E map(RowMapper<T> mapper) {
        this.mapper = mapper;
        return (E) this;
    }

    @Override
    public R execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public R execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            binder.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return extractor.apply(rs, mapper);
            }
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }

    public static <T> SelectListPath<T> asList(ConnectionSupplier supplier, String sql) {
        return new SelectListPath<>(supplier, sql);
    }

    public static <T> SelectSinglePath<T> asSingle(ConnectionSupplier supplier, String sql) {
        return new SelectSinglePath<>(supplier, sql);
    }

    public static class SelectListPath<T> extends SelectPath<T, List<T>, ListExecutor<T>> implements ListExecutor<T> {
        public SelectListPath(ConnectionSupplier supplier, String sql) {
            super(supplier, sql, (rs, mapper) -> {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapper.apply(rs));
                }
                return list;
            });
        }
    }

    public static class SelectSinglePath<T> extends SelectPath<T, Optional<T>, Executor<Optional<T>>> {
        public SelectSinglePath(ConnectionSupplier supplier, String sql) {
            super(supplier, sql, (rs, mapper) -> {
                if (rs.next()) {
                    return Optional.of(mapper.apply(rs));
                }
                return Optional.empty();
            });
        }
    }
}
