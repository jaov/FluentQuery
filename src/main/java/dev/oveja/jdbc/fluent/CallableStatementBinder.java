package dev.oveja.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableStatementBinder extends ThrowingConsumer<CallableStatement, SQLException> {
}
