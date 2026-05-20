package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.*;
import dev.oveja.jdbc.fluent.RowMapper;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsertReturningPath<T> extends BaseStatementPath<PreparedStatement, InsertReturningPath<T>> {

    private final ConnectionSupplier supplier;
    private final String sql;

    public InsertReturningPath(ConnectionSupplier supplier, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    protected InsertReturningPath<T> self() {
        return this;
    }

    public ListExecutor<T> map(RowMapper<T> mapper) {
        return new ListExecutor<T>() {
            @Override public List<T> execute() throws SQLException { return execute(supplier); }
            @Override public List<T> execute(ConnectionSupplier s) throws SQLException {
                return executeInternal(s, mapper);
            }
        };
    }

    public Executor<Optional<T>> mapOne(RowMapper<T> mapper) {
        return new Executor<Optional<T>>() {
            @Override public Optional<T> execute() throws SQLException { return execute(supplier); }
            @Override public Optional<T> execute(ConnectionSupplier s) throws SQLException {
                return executeInternalOne(s, mapper);
            }
        };
    }

    public ListExecutor<Integer> mapInt() {
        return new ListExecutor<Integer>() {
            @Override public List<Integer> execute() throws SQLException { return execute(supplier); }
            @Override public List<Integer> execute(ConnectionSupplier s) throws SQLException {
                return executeInternal(s, rs -> rs.getInt(1));
            }
        };
    }

    public Executor<Optional<Integer>> mapOneInt() {
        return new Executor<Optional<Integer>>() {
            @Override public Optional<Integer> execute() throws SQLException { return execute(supplier); }
            @Override public Optional<Integer> execute(ConnectionSupplier s) throws SQLException {
                return executeInternalOne(s, rs -> rs.getInt(1));
            }
        };
    }

    public ListExecutor<Long> mapLong() {
        return new ListExecutor<Long>() {
            @Override public List<Long> execute() throws SQLException { return execute(supplier); }
            @Override public List<Long> execute(ConnectionSupplier s) throws SQLException {
                return executeInternal(s, rs -> rs.getLong(1));
            }
        };
    }

    public Executor<Optional<Long>> mapOneLong() {
        return new Executor<Optional<Long>>() {
            @Override public Optional<Long> execute() throws SQLException { return execute(supplier); }
            @Override public Optional<Long> execute(ConnectionSupplier s) throws SQLException {
                return executeInternalOne(s, rs -> rs.getLong(1));
            }
        };
    }

    public ListExecutor<String> mapString() {
        return new ListExecutor<String>() {
            @Override public List<String> execute() throws SQLException { return execute(supplier); }
            @Override public List<String> execute(ConnectionSupplier s) throws SQLException {
                return executeInternal(s, rs -> rs.getString(1));
            }
        };
    }

    public Executor<Optional<String>> mapOneString() {
        return new Executor<Optional<String>>() {
            @Override public Optional<String> execute() throws SQLException { return execute(supplier); }
            @Override public Optional<String> execute(ConnectionSupplier s) throws SQLException {
                return executeInternalOne(s, rs -> rs.getString(1));
            }
        };
    }

    private <R> List<R> executeInternal(ConnectionSupplier s, RowMapper<R> m) throws SQLException {
        Connection con = s.get();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            applyBinders(ps);
            boolean hasResultSet = ps.execute();
            List<R> list = new ArrayList<>();
            if (hasResultSet) {
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) list.add(m.map(rs));
                }
            }
            return list;
        } finally {
            if (s.shouldClose()) con.close();
        }
    }

    private <R> Optional<R> executeInternalOne(ConnectionSupplier s, RowMapper<R> m) throws SQLException {
        Connection con = s.get();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            applyBinders(ps);
            boolean hasResultSet = ps.execute();
            if (hasResultSet) {
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) return Optional.of(m.map(rs));
                }
            }
            return Optional.empty();
        } finally {
            if (s.shouldClose()) con.close();
        }
    }
}
