package dev.j8a.jdbc.fluent.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;

import dev.j8a.jdbc.fluent.CallableMapper;
import dev.j8a.jdbc.fluent.ConnectionSupplier;
import dev.j8a.jdbc.fluent.api.CallBinder;
import dev.j8a.jdbc.fluent.api.CallMapper;
import dev.j8a.jdbc.fluent.api.QueryExecutor;

public class CallPath<T>
        extends BaseStatementPath<CallableStatement, CallBinder<T>>
        implements CallBinder<T>, CallMapper<T>, QueryExecutor<T> {

    private final String sql;
    private final ConnectionSupplier supplier;
    private CallableMapper<T> mapper;

    public CallPath(ConnectionSupplier supplier, Class<T> ignoredClass, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    protected CallBinder<T> self() {
        return this;
    }

    @Override
    public CallBinder<T> bindOut(int sqlType) {
        int idx = currentIndex++;
        return bind(cs -> cs.registerOutParameter(idx, sqlType));
    }

    @Override
    public CallBinder<T> bindOut(SQLType sqlType) {
        int idx = currentIndex++;
        return bind(cs -> cs.registerOutParameter(idx, sqlType));
    }

    @Override
    public CallMapper<T> noParams() {
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
            public Void execute(ConnectionSupplier supplier) throws SQLException {
                return CallPath.this.runVoid(supplier);
            }

            @Override
            public Void execute() throws SQLException {
                return execute(CallPath.this.supplier);
            }
        };
    }

    private Void runVoid(ConnectionSupplier supplier) throws SQLException {
        this.execute(supplier);
        return null;
    }

    @Override
    public T execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public T execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (CallableStatement stmt = con.prepareCall(sql)) {
            applyBinders(stmt);
            stmt.execute();
            return mapper != null ? mapper.map(stmt) : null;
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
