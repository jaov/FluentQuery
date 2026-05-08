package dev.oveja.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableMapper<T> extends ThrowingFunction<CallableStatement,T, SQLException> {
}
