package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlPath implements 
        Binder<PreparedStatement, Executor<Integer>>, 
        Executor<Integer> {

    private final ConnectionSupplier supplier;
    private final String sql;
    private ThrowingConsumer<PreparedStatement, SQLException> binder = ps -> {};

    public DmlPath(ConnectionSupplier supplier, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    public Executor<Integer> bind(ThrowingConsumer<PreparedStatement, SQLException> binder) {
        this.binder = binder;
        return this;
    }

    @Override
    public Integer execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public Integer execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            binder.accept(ps);
            return ps.executeUpdate();
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
