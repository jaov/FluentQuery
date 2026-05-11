package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.ThrowingFunction;
import dev.oveja.jdbc.fluent.api.CallBinder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;

public class CallPath<T> 
        extends BaseStatementPath<CallableStatement, CallBinder<T>>
        implements CallBinder<T> {

    private final String sql;
    private final ConnectionSupplier supplier;
    private ThrowingFunction<CallableStatement, T, SQLException> mapper;

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
    public Executor<T> map(ThrowingFunction<CallableStatement, T, SQLException> mapper) {
        this.mapper = mapper;
        return this;
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
            return mapper != null ? mapper.apply(stmt) : null;
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
