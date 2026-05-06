package dev.oveja.jdbc.fluent.interfaces.dml;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;
import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;

import java.sql.SQLException;

public interface DmlStatementExecutor {
    int execute() throws SQLException;
    int execute(ConnectionSupplier supplier) throws SQLException;

    default ThrowingFunction<ConnectionSupplier, Integer, SQLException> asFunction() {
        return this::execute;
    }

    default ThrowingConsumer<ConnectionSupplier, SQLException> asConsumer() {
        return this::execute;
    }
}
