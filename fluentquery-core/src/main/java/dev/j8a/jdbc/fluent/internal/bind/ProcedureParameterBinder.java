package dev.j8a.jdbc.fluent.internal.bind;

import java.sql.CallableStatement;

@FunctionalInterface
public interface ProcedureParameterBinder extends StatementBinder<CallableStatement> {
}
