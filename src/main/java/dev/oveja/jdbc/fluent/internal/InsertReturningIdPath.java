package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.QueryBinder;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ThrowingFunction;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import dev.oveja.jdbc.fluent.RowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningIdPath<T> 
        extends BasePreparedStatementPath<QueryBinder<T, ListExecutor<T>>>
        implements QueryBinder<T, ListExecutor<T>>, ListExecutor<T> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private RowMapper<T> mapper;

    public InsertReturningIdPath(ConnectionSupplier supplier, Class<T> ignoredClazz, RowMapper<T> mapper, String sql) {
        this.supplier = supplier;
        this.mapper = mapper;
        this.sql = sql;
    }

    @Override
    protected QueryBinder<T, ListExecutor<T>> self() {
        return this;
    }

    @Override
    public ListExecutor<T> map(RowMapper<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public List<T> execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public List<T> execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            applyBinders(ps);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                List<T> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapper.apply(rs));
                }
                return list;
            }
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
