package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.CallableMapper;
import dev.oveja.jdbc.fluent.ResultMapper;
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
    default Executor<T> map(ResultMapper<CallableStatement, T> mapper) {
        return map((CallableMapper<T>) mapper::map);
    }

    Executor<T> map(CallableMapper<T> mapper);
}
