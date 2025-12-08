package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableMapper<T> extends ThrowingFunction<CallableStatement,T, SQLException> {
}
