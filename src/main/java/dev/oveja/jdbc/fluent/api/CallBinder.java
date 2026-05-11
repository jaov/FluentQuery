package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ThrowingFunction;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLType;

public interface CallBinder<T> extends 
        FluentBinder<CallableStatement, CallBinder<T>>,
        Mapper<CallableStatement, T, Executor<T>>,
        Executor<T> {

    CallBinder<T> bindOut(int sqlType);
    CallBinder<T> bindOut(SQLType sqlType);
    
    @Override
    Executor<T> map(ThrowingFunction<CallableStatement, T, SQLException> mapper);
}
