package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.api.Binder;
import dev.oveja.jdbc.fluent.api.Executor;
import dev.oveja.jdbc.fluent.api.Mapper;
import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.CallableMapper;
import dev.oveja.jdbc.fluent.ConnectionSupplier;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

public class CallPath<T> implements 
        Binder<CallableStatement, CallPath<T>>, 
        Executor<T> {

    private final String sql;
    private final ConnectionSupplier supplier;
    private ThrowingConsumer<CallableStatement, SQLException> inBinder = cs -> {};
    private final List<ThrowingConsumer<CallableStatement, SQLException>> outBinders = new ArrayList<>();
    private CallableMapper<T> mapper;


    public CallPath(ConnectionSupplier supplier, Class<T> ignoredClass, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    public CallPath<T> bind(ThrowingConsumer<CallableStatement, SQLException> binder) {
        this.inBinder = binder;
        return this;
    }

    public CallPath<T> registerOut(int index, SQLType sqlType) {
        outBinders.add(cs -> cs.registerOutParameter(index, sqlType.getVendorTypeNumber()));
        return this;
    }

    public CallPath<T> registerOut(int index, SQLType sqlType, int scale) {
        outBinders.add(cs -> cs.registerOutParameter(index, sqlType.getVendorTypeNumber(), scale));
        return this;
    }

    public CallPath<T> registerOut(ThrowingConsumer<CallableStatement, SQLException> binder) {
        outBinders.add(binder);
        return this;
    }

    public Executor<T> map(CallableMapper<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    public Executor<Void> run() {
        return new Executor<Void>() {
            @Override
            public Void execute() throws SQLException {
                return execute(supplier);
            }

            @Override
            public Void execute(ConnectionSupplier supplier) throws SQLException {
                CallPath.this.execute(supplier);
                return null;
            }
        };
    }

    @Override
    public T execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public T execute(ConnectionSupplier supplier) throws SQLException {
        Connection con = supplier.get();
        try (CallableStatement stmt = con.prepareCall(sql)) {
            inBinder.accept(stmt);

            for (ThrowingConsumer<CallableStatement, SQLException> binder : outBinders) {
                binder.accept(stmt);
            }

            stmt.execute();

            return mapper != null ? mapper.apply(stmt) : null;

        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
