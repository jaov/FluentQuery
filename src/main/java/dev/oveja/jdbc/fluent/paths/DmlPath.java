package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementExecutor;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementBinder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlPath implements DmlStatementExecutor, DmlStatementBinder {
    private final String sql;
    private final ConnectionSupplier supplier;
    private ParameterBinder binder;

    public DmlPath(ConnectionSupplier supplier, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    public int execute() throws SQLException {
        Connection con = supplier.get();
        try (PreparedStatement stmt = con.prepareStatement(this.sql)) {
            binder.accept(stmt);
            return stmt.executeUpdate();
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }

    @Override
    public DmlStatementExecutor bind(ParameterBinder binder) {
        this.binder = binder;
        return this;
    }
}
