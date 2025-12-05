package dev.oveja.jdbc.fluent.interfaces.call;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallStart<T>{
    CallInParam<T> bind(ThrowingConsumer<CallableStatement, SQLException> binder);
}
