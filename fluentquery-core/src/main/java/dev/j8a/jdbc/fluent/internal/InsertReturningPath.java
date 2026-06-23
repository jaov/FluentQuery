package dev.j8a.jdbc.fluent.internal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.RowMapper;
import dev.j8a.jdbc.fluent.api.*;

public class InsertReturningPath<T> extends BaseStatementPath<PreparedStatement, InsertReturningPath<T>> {

    private final String sql;
    private final boolean useGeneratedKeys;

    public InsertReturningPath(String sql, boolean useGeneratedKeys) {
        this.sql = sql;
        this.useGeneratedKeys = useGeneratedKeys;
    }

    @Override
    protected InsertReturningPath<T> self() {
        return this;
    }

    public QueryMapper<T> map(RowMapper<T> mapper) {
        return createQueryMapper(mapper);
    }

    private <R> QueryMapper<R> createQueryMapper(RowMapper<R> mapper) {
        return new QueryMapper<R>() {
            @Override public ListQueryExecutor<R> list() {
                return new ListQueryExecutor<R>() {
                    private java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer;

                    @Override
                    public ListQueryExecutor<R> log(java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) {
                        this.logConsumer = logConsumer;
                        return this;
                    }

                    @Override public List<R> execute() throws SQLException { return execute(ConnectionSupplierLoader.load()); }
                    @Override public List<R> execute(ConnectionSupplier s) throws SQLException {
                        return executeInternal(s, mapper, logConsumer);
                    }
                };
            }
            @Override public QueryExecutor<Optional<R>> one() {
                return new QueryExecutor<Optional<R>>() {
                    private java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer;

                    @Override
                    public QueryExecutor<Optional<R>> log(java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) {
                        this.logConsumer = logConsumer;
                        return this;
                    }

                    @Override public Optional<R> execute() throws SQLException { return execute(ConnectionSupplierLoader.load()); }
                    @Override public Optional<R> execute(ConnectionSupplier s) throws SQLException {
                        return executeInternalOne(s, mapper, logConsumer);
                    }
                };
            }
        };
    }

    public QueryMapper<Integer> mapInt() {
        return createQueryMapper(rs -> rs.getInt(1));
    }

    public QueryMapper<Long> mapLong() {
        return createQueryMapper(rs -> rs.getLong(1));
    }

    public QueryMapper<String> mapString() {
        return createQueryMapper(rs -> rs.getString(1));
    }

    private <R> List<R> executeInternal(ConnectionSupplier s, RowMapper<R> m, java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) throws SQLException {
        Connection con = s.get();
        long start = System.nanoTime();
        try (PreparedStatement ps = prepare(con)) {
            applyBinders(ps);
            ResultSet rs = executeAndGetResultSet(ps);
            List<R> list = new ArrayList<>();
            if (rs != null) {
                try {
                    while (rs.next()) list.add(m.map(rs));
                } finally {
                    rs.close();
                }
            }
            if (logConsumer != null) {
                long duration = System.nanoTime() - start;
                logConsumer.accept(new dev.j8a.jdbc.fluent.api.QueryContext(sql, boundParameters, ps.toString(), duration));
            }
            return list;
        } finally {
            if (s.shouldClose()) con.close();
        }
    }

    private <R> Optional<R> executeInternalOne(ConnectionSupplier s, RowMapper<R> m, java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) throws SQLException {
        Connection con = s.get();
        long start = System.nanoTime();
        try (PreparedStatement ps = prepare(con)) {
            applyBinders(ps);
            ResultSet rs = executeAndGetResultSet(ps);
            Optional<R> result = Optional.empty();
            if (rs != null) {
                try {
                    if (rs.next()) result = Optional.of(m.map(rs));
                } finally {
                    rs.close();
                }
            }
            if (logConsumer != null) {
                long duration = System.nanoTime() - start;
                logConsumer.accept(new dev.j8a.jdbc.fluent.api.QueryContext(sql, boundParameters, ps.toString(), duration));
            }
            return result;
        } finally {
            if (s.shouldClose()) con.close();
        }
    }

    private PreparedStatement prepare(Connection con) throws SQLException {
        if (useGeneratedKeys) {
            return con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            return con.prepareStatement(sql);
        }
    }

    private ResultSet executeAndGetResultSet(PreparedStatement ps) throws SQLException {
        if (useGeneratedKeys) {
            ps.executeUpdate();
            return ps.getGeneratedKeys();
        } else {
            boolean hasResultSet = ps.execute();
            return hasResultSet ? ps.getResultSet() : null;
        }
    }
}
