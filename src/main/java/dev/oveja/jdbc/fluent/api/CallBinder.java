package dev.oveja.jdbc.fluent.api;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.SQLType;


import dev.oveja.jdbc.fluent.CallableMapper;

public interface CallBinder<T> extends FluentBinder<CallableStatement, CallBinder<T>>, CallMapper<T> {

    CallBinder<T> bindOut(int sqlType);
    
    CallBinder<T> bindOut(SQLType sqlType);

    CallMapper<T> noParams();
}
