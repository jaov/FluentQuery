package dev.j8a.jdbc.fluent.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.internal.ConnectionSupplierLoader;
import dev.j8a.jdbc.fluent.api.DmlBinder;

/**
 * Handles DML statements (INSERT, UPDATE, DELETE).
 * Execution can be performed with an explicit {@link ConnectionSupplier} or the default loader.
 */
public class DmlPath extends BaseStatementPath<PreparedStatement, DmlBinder> implements DmlBinder {
    private final String sql;

    public DmlPath(String sql) {
        this.sql = sql;
    }

    @Override
    protected DmlBinder self() {
        return this;
    }

    @Override
    public Integer execute() throws SQLException {
        // Use the default ConnectionSupplierLoader
        return execute(ConnectionSupplierLoader.load());
    }

    @Override
    public Integer execute(ConnectionSupplier supplier) throws SQLException {
        if (supplier == null) {
            throw new IllegalArgumentException("ConnectionSupplier cannot be null");
        }
        System.out.println("DmlPath: Calling execute(ConnectionSupplier)");
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
