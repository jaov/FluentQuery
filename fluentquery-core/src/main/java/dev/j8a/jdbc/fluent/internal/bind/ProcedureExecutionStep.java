package dev.j8a.jdbc.fluent.internal.bind;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ProcedureExecutionStep {
    void execute(CallableStatement cs) throws SQLException;
}