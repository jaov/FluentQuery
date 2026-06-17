package dev.j8a.jdbc.fluent;

import java.sql.SQLException;

public interface BorrowedConnectionSupplier extends ConnectionSupplier {
    void commit() throws SQLException;
    void rollback() throws SQLException;
}
