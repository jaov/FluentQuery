package dev.j8a.jdbc.fluent.internal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.RowMapper;
import dev.j8a.jdbc.fluent.api.*;

public class InsertReturningPath<T> extends BaseStatementPath<PreparedStatement, InsertReturningPath<T>> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private final boolean useGeneratedKeys;

    public InsertReturningPath(ConnectionSupplier supplier, String sql, boolean useGeneratedKeys) {
        this.supplier = supplier;
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
            @Override public ListExecutor<R> list() {
                return new ListExecutor<R>() {
                    @Override public List<R> execute() throws SQLException { return execute(supplier); }
                    @Override public List<R> execute(ConnectionSupplier s) throws SQLException {
                        return executeInternal(s, mapper);
                    }
                };
            }
            @Override public Executor<Optional<R>> one() {
                return new Executor<Optional<R>>() {
                    @Override public Optional<R> execute() throws SQLException { return execute(supplier); }
                    @Override public Optional<R> execute(ConnectionSupplier s) throws SQLException {
                        return executeInternalOne(s, mapper);
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

    private <R> List<R> executeInternal(ConnectionSupplier s, RowMapper<R> m) throws SQLException {
        Connection con = s.get();
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
            return list;
        } finally {
            if (s.shouldClose()) con.close();
        }
    }

    private <R> Optional<R> executeInternalOne(ConnectionSupplier s, RowMapper<R> m) throws SQLException {
        Connection con = s.get();
        try (PreparedStatement ps = prepare(con)) {
            applyBinders(ps);
            ResultSet rs = executeAndGetResultSet(ps);
            if (rs != null) {
                try {
                    if (rs.next()) return Optional.of(m.map(rs));
                } finally {
                    rs.close();
                }
            }
            return Optional.empty();
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
