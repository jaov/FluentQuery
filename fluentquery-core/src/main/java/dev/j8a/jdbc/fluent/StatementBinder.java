package dev.j8a.jdbc.fluent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementBinder<S extends PreparedStatement> {
    void bind(S statement) throws SQLException;
}
