package dev.j8a.jdbc.fluent.internal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.api.DmlParameterMapCreator;
import dev.j8a.jdbc.fluent.api.QueryContext;
import dev.j8a.jdbc.fluent.internal.bind.PsBinderCreator;

/**
 * Handles DML statements (INSERT, UPDATE, DELETE).
 * Execution can be performed with an explicit {@link ConnectionSupplier} or the default loader.
 */
public class DmlPath extends BaseStatementPath<PreparedStatement, DmlParameterMapCreator> implements DmlParameterMapCreator {
    private final String sql;

    public DmlPath(String sql) {
        this.sql = sql;
        this.binderCreator = new PsBinderCreator();
    }

    @Override
    protected DmlParameterMapCreator self() {
        return this;
    }

    @Override
    public DmlParameterMapCreator log(Consumer<QueryContext> logConsumer) {
        this.logConsumer = logConsumer;
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

        Connection con = supplier.get();
        long start = System.nanoTime();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            binderCreator.create(getBoundParameters()).bind(ps);

            int rows = ps.executeUpdate();
            if (logConsumer != null) {
                long duration = System.nanoTime() - start;
                logConsumer.accept(new QueryContext(sql, getBoundParameters(), ps.toString(), duration));
            }
            return rows;
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
