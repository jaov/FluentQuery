package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.api.Mapper;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningPath<T> implements 
        Binder<PreparedStatement, Mapper<ResultSet, T, ListExecutor<T>>>, 
        Mapper<ResultSet, T, ListExecutor<T>>, 
        ListExecutor<T> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private ThrowingConsumer<PreparedStatement, SQLException> binder = ps -> {};
    private RowMapper<T> mapper;

    public InsertReturningPath(ConnectionSupplier supplier, Class<T> ignoredClazz, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    public Mapper<ResultSet, T, ListExecutor<T>> bind(ThrowingConsumer<PreparedStatement, SQLException> binder) {
        this.binder = binder;
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
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            binder.accept(ps);
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
