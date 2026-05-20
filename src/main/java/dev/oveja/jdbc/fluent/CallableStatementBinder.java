package dev.oveja.jdbc.fluent;

import java.sql.CallableStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface CallableStatementBinder extends StatementBinder<java.sql.CallableStatement> {
    @Override
    void bind(java.sql.CallableStatement cs) throws SQLException;
}
