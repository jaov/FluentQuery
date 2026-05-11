package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.*;
import dev.oveja.jdbc.fluent.ThrowingBiFunction;
import dev.oveja.jdbc.fluent.ThrowingFunction;
import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SelectPath<T, R, E extends Executor<R>> 
        extends BaseStatementPath<PreparedStatement, QueryBinder<T, E>>
        implements QueryBinder<T, E>, Executor<R> {

    protected final ConnectionSupplier supplier;
    protected final String sql;
    protected ThrowingFunction<ResultSet, T, SQLException> mapper;
    protected final ThrowingBiFunction<ResultSet, ThrowingFunction<ResultSet, T, SQLException>, R, SQLException> extractor;


    protected SelectPath(ConnectionSupplier supplier, String sql, ThrowingBiFunction<ResultSet, ThrowingFunction<ResultSet, T, SQLException>, R, SQLException> extractor) {
        this.supplier = supplier;
        this.sql = sql;
        this.extractor = extractor;
    }

    @Override
    public E map(ThrowingFunction<ResultSet, T, SQLException> mapper) {
        this.mapper = mapper;
        return selfExecutor();
    }

    protected abstract E selfExecutor();

    @Override
    public R execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public R execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            applyBinders(ps);
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

        @Override
        protected QueryBinder<T, ListExecutor<T>> self() {
            return this;
        }

        @Override
        protected ListExecutor<T> selfExecutor() {
            return this;
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

        @Override
        protected QueryBinder<T, Executor<Optional<T>>> self() {
            return this;
        }

        @Override
        protected Executor<Optional<T>> selfExecutor() {
            return this;
        }
    }
}
