package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.QueryBinder;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.ThrowingFunction;
import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningPath<T> 
        extends BaseStatementPath<PreparedStatement, QueryBinder<T, ListExecutor<T>>>
        implements QueryBinder<T, ListExecutor<T>>, ListExecutor<T> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private ThrowingFunction<ResultSet, T, SQLException> mapper;

    public InsertReturningPath(ConnectionSupplier supplier, Class<T> ignoredClazz, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    protected QueryBinder<T, ListExecutor<T>> self() {
        return this;
    }

    @Override
    public ListExecutor<T> map(ThrowingFunction<ResultSet, T, SQLException> mapper) {
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
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            applyBinders(ps);
            try (ResultSet rs = ps.executeQuery()) {
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
