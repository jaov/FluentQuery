package dev.oveja.jdbc.fluent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterBinder extends StatementBinder<PreparedStatement> {
    @Override
    void bind(PreparedStatement ps) throws SQLException;
}
