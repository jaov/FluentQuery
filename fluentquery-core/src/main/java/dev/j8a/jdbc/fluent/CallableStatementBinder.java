package dev.j8a.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface CallableStatementBinder extends StatementBinder<CallableStatement> {
    @Override
    void bind(CallableStatement cs) throws SQLException;
}
