package dev.oveja.jdbc.fluent.interfaces.call;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.CallableMapper;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.CallableStatementBinder;

import java.sql.SQLException;
import java.sql.SQLType;

public interface CallOutParam<T> {
    CallOutParam<T> registerOut(int index, SQLType sqlType);
    CallOutParam<T> registerOut(int index, SQLType sqlType, int scale);
    CallOutParam<T> registerOut(CallableStatementBinder binder);
    CallExecutor<T> map(CallableMapper<T> mapper);
    CallExecutor<Void> run() throws SQLException;
}
