package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementExecutor;
import dev.oveja.jdbc.fluent.interfaces.dml.DmlStatementBinder;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DmlPath implements DmlStatementExecutor, DmlStatementBinder {
    private final String sql;
    private ParameterBinder binder;

    public DmlPath(String sql) {
        this.sql = sql;
    }

    @Override
    public int execute() throws SQLException {
        try(Connection con = ConnectionSupplierLoader.load().get();
            PreparedStatement stmt = con.prepareStatement(this.sql)) {
            binder.accept(stmt);
            return stmt.executeUpdate();
        }
    }

    @Override
    public DmlStatementExecutor bind(ParameterBinder binder) {
        this.binder = binder;
        return this;
    }
}
