package dev.j8a.jdbc.fluent.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.CallableMapper;
import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.api.CallParameterMapCreator;
import dev.j8a.jdbc.fluent.api.CallMapper;
import dev.j8a.jdbc.fluent.api.QueryContext;
import dev.j8a.jdbc.fluent.api.QueryExecutor;
import dev.j8a.jdbc.fluent.internal.bind.ProcedureBinderCreator;
import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

/**
 * Path handling for callable statements (stored procedures / functions).
 * Allows execution with an explicit {@link ConnectionSupplier} or falls back to the default
 * {@link ConnectionSupplierLoader} when none is supplied.
 */
public class CallPath<T> extends BaseStatementPath<CallableStatement, CallMapper<T>> implements CallParameterMapCreator<T>, CallMapper<T>, QueryExecutor<T> {
    private final String sql;
    private CallableMapper<T> mapper;

    public CallPath(Class<T> ignoredClass, String sql) {
        this.sql = sql;
        this.binderCreator = new ProcedureBinderCreator();
    }

    @Override
    protected CallParameterMapCreator<T> self() {
        return this;
    }

    @Override
    public CallMapper<T> bind(Object first, Object... rest) {
        super.bind(first,rest);
        return this;
    }

    private Consumer<QueryContext> logConsumer;

    @Override
    public QueryExecutor<T> log(Consumer<QueryContext> logConsumer) {
        this.logConsumer = logConsumer;
        return this;
    }

    @Override
    public QueryExecutor<T> map(CallableMapper<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public QueryExecutor<Void> voidCall() {
        this.mapper = null;
        return new QueryExecutor<Void>() {

            @Override
            public QueryExecutor<Void> log(Consumer<QueryContext> logConsumer) {
                CallPath.this.log(logConsumer);
                return this;
            }

            @Override
            public Void execute(ConnectionSupplier supplier) throws SQLException {
                return CallPath.this.runVoid(supplier);
            }

            @Override
            public Void execute() throws SQLException {
                return execute(ConnectionSupplierLoader.load());
            }
        };
    }

    private Void runVoid(ConnectionSupplier supplier) throws SQLException {
        this.execute(supplier);
        return null;
    }

    @Override
    public T execute() throws SQLException {
        // Fallback to default loader
        return execute(ConnectionSupplierLoader.load());
    }

    @Override
    public T execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        long start = System.nanoTime();
        try (CallableStatement stmt = con.prepareCall(sql)) {
            Map<ParameterKey,Object> boundParameters = getBoundParameters();
            if(!boundParameters.isEmpty()) {
                binderCreator.create(boundParameters).bind(stmt);
            }
            stmt.execute();
            T result = mapper != null ? mapper.map(stmt) : null;
            if (logConsumer != null) {
                long duration = System.nanoTime() - start;
                logConsumer.accept(new QueryContext(sql, boundParameters, stmt.toString(), duration));
            }

            return result;
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
