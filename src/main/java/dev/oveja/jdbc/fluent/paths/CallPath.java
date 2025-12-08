package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.call.CallExecutor;
import dev.oveja.jdbc.fluent.interfaces.call.CallInParam;
import dev.oveja.jdbc.fluent.interfaces.call.CallOutParam;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.CallableMapper;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.CallableStatementBinder;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

public class CallPath<T> implements CallInParam<T>, CallOutParam<T> {

    private final String sql;
    private CallableStatementBinder inBinder;
    private final List<CallableStatementBinder> outBinders;


    public CallPath(Class<T> ignoredClass, String sql) {
        this.outBinders = new ArrayList<>();
        this.sql = sql;
    }

    @Override
    public CallOutParam<T> setInParam(CallableStatementBinder binder) {
        this.inBinder = binder;
        return this;
    }

    @Override
    public CallOutParam<T> registerOut(int index, SQLType sqlType) {
        outBinders.add(cs -> cs.registerOutParameter(index,sqlType.getVendorTypeNumber()));
        return this;
    }

    @Override
    public CallOutParam<T> registerOut(int index, SQLType sqlType, int scale) {
        outBinders.add(cs -> cs.registerOutParameter(index,sqlType.getVendorTypeNumber(),scale));
        return this;
    }

    @Override
    public CallOutParam<T> registerOut(CallableStatementBinder binder) {
        outBinders.add(binder);
        return this;
    }

    @Override
    public CallExecutor<T> map(CallableMapper<T> mapper) {
        return new CallExecutorImpl<>(mapper);
    }

    @Override
    public CallExecutor<Void> run() {
        return new CallExecutorImpl<>(cs -> null);
    }

    private class CallExecutorImpl<R> implements CallExecutor<R> {

        private final CallableMapper<R> mapper;

        private CallExecutorImpl(CallableMapper<R> mapper) {
            this.mapper = mapper;
        }

        @Override
        public R execute() throws SQLException {
            try(Connection con = ConnectionSupplierLoader.load().get();
                CallableStatement stmt = con.prepareCall(sql)){
                inBinder.accept(stmt);

                for (CallableStatementBinder binder : outBinders) {
                    binder.accept(stmt);
                }

                stmt.execute();

                return this.mapper.apply(stmt);

            }
        }
    }

}
