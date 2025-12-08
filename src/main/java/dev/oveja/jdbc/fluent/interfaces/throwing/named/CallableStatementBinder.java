package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableStatementBinder extends ThrowingConsumer<CallableStatement, SQLException> {
}
