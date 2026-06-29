package dev.j8a.jdbc.fluent.internal.bind;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PsParameterBinder extends StatementBinder<PreparedStatement> {
    @Override
    void bind(PreparedStatement ps) throws SQLException;
}
