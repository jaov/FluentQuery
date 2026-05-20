package dev.oveja.jdbc.fluent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterBinder extends StatementBinder<java.sql.PreparedStatement> {
    @Override
    void bind(java.sql.PreparedStatement ps) throws SQLException;
}
