package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.ListExecutor;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ConnectionSupplier;
import dev.oveja.jdbc.fluent.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningIdPath<T> implements 
        Binder<PreparedStatement, ListExecutor<T>>, 
        ListExecutor<T> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private final RowMapper<T> mapper;
    private ThrowingConsumer<PreparedStatement, SQLException> binder = ps -> {};

    public InsertReturningIdPath(ConnectionSupplier supplier, Class<T> ignoredClazz, RowMapper<T> mapper, String sql) {
        this.supplier = supplier;
        this.mapper = mapper;
        this.sql = sql;
    }

    @Override
    public ListExecutor<T> bind(ThrowingConsumer<PreparedStatement, SQLException> binder) {
        this.binder = binder;
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
            binder.accept(ps);
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
