package dev.oveja.jdbc.fluent.interfaces.dml;

import java.sql.SQLException;

public interface DmlStatementExecutor {
    int execute() throws SQLException;
}
