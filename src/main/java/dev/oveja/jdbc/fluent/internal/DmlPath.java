package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.DmlBinder;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlPath extends BasePreparedStatementPath<DmlBinder> implements DmlBinder {

    private final ConnectionSupplier supplier;
    private final String sql;

    public DmlPath(ConnectionSupplier supplier, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    protected DmlBinder self() {
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
            applyBinders(ps);
            return ps.executeUpdate();
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
