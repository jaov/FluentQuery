package dev.j8a.jdbc.fluent.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.RowMapper;
import dev.j8a.jdbc.fluent.api.ListQueryExecutor;
import dev.j8a.jdbc.fluent.api.QueryExecutor;
import dev.j8a.jdbc.fluent.api.QueryMapper;

public class SelectPath<T>
        extends BaseStatementPath<PreparedStatement, SelectPath<T>> {

    private final String sql;

    public SelectPath(String sql) {
        this.sql = sql;
    }

    @Override
    protected SelectPath<T> self() {
        return this;
    }

    public QueryMapper<T> map(RowMapper<T> mapper) {
        return new QueryMapper<T>() {
            @Override
            public ListQueryExecutor<T> list() {
                return new ListQueryExecutor<T>() {
                    @Override
                    public List<T> execute() throws SQLException {
                        return execute(ConnectionSupplierLoader.load());
                    }

                    @Override
                    public List<T> execute(ConnectionSupplier s) throws SQLException {
                        Connection con = s.get();
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                            applyBinders(ps);
                            try (ResultSet rs = ps.executeQuery()) {
                                List<T> list = new ArrayList<>();
                                while (rs.next()) {
                                    list.add(mapper.map(rs));
                                }
                                return list;
                            }
                        } finally {
                            if (s.shouldClose()) {
                                con.close();
                            }
                        }
                    }
                };
            }

            @Override
            public QueryExecutor<Optional<T>> one() {
                return new QueryExecutor<Optional<T>>() {
                    @Override
                    public Optional<T> execute() throws SQLException {
                        return execute(ConnectionSupplierLoader.load());
                    }

                    @Override
                    public Optional<T> execute(ConnectionSupplier s) throws SQLException {
                        Connection con = s.get();
                        try (PreparedStatement ps = con.prepareStatement(sql)) {
                            applyBinders(ps);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    return Optional.of(mapper.map(rs));
                                }
                                return Optional.empty();
                            }
                        } finally {
                            if (s.shouldClose()) {
                                con.close();
                            }
                        }
                    }
                };
            }
        };
    }
}
