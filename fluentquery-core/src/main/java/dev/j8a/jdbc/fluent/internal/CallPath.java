package dev.j8a.jdbc.fluent.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;

import dev.j8a.jdbc.fluent.CallableMapper;
import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.internal.ConnectionSupplierLoader;
import dev.j8a.jdbc.fluent.api.CallBinder;
import dev.j8a.jdbc.fluent.api.CallMapper;
import dev.j8a.jdbc.fluent.api.QueryExecutor;

/**
 * Path handling for callable statements (stored procedures / functions).
 * Allows execution with an explicit {@link ConnectionSupplier} or falls back to the default
 * {@link ConnectionSupplierLoader} when none is supplied.
 */
public class CallPath<T> extends BaseStatementPath<CallableStatement, CallBinder<T>> implements CallBinder<T>, CallMapper<T>, QueryExecutor<T> {
    private final String sql;
    private CallableMapper<T> mapper;

    public CallPath(Class<T> ignoredClass, String sql) {
        this.sql = sql;
    }

    @Override
    protected CallBinder<T> self() {
        return this;
    }

    @Override
    public CallBinder<T> bindOut(int sqlType) {
        setBindMode(BindMode.SEQUENTIAL);
        int idx = currentIndex++;
        return addBinder(cs -> cs.registerOutParameter(idx, sqlType));
    }

    @Override
    public CallBinder<T> bindOut(SQLType sqlType) {
        setBindMode(BindMode.SEQUENTIAL);
        int idx = currentIndex++;
        return addBinder(cs -> cs.registerOutParameter(idx, sqlType));
    }

    @Override
    public CallMapper<T> noParams() {
        return this;
    }

    private java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer;

    @Override
    public QueryExecutor<T> log(java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) {
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
            private java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> voidLogConsumer;

            @Override
            public QueryExecutor<Void> log(java.util.function.Consumer<dev.j8a.jdbc.fluent.api.QueryContext> logConsumer) {
                this.voidLogConsumer = logConsumer;
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
            applyBinders(stmt);
            stmt.execute();
            T result = mapper != null ? mapper.map(stmt) : null;
            if (logConsumer != null) {
                long duration = System.nanoTime() - start;
                logConsumer.accept(new dev.j8a.jdbc.fluent.api.QueryContext(sql, boundParameters, stmt.toString(), duration));
            }
            return result;
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
