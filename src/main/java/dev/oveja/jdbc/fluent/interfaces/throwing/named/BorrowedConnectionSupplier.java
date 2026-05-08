package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import java.sql.SQLException;

public interface BorrowedConnectionSupplier extends ConnectionSupplier {
    void commit() throws SQLException;
    void rollback() throws SQLException;
}
