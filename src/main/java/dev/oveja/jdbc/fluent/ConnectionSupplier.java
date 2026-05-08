package dev.oveja.jdbc.fluent;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier extends ThrowingSupplier<Connection,SQLException> {

    default boolean shouldClose() {
        return true;
    }

    static BorrowedConnectionSupplier borrowed(Connection con) {
        return new BorrowedConnectionSupplier() {
            @Override
            public Connection get() throws SQLException {
                return con;
            }

            @Override
            public boolean shouldClose() {
                return false;
            }

            @Override
            public void commit() throws SQLException {
                con.commit();
            }

            @Override
            public void rollback() throws SQLException {
                con.rollback();
            }
        };
    }
}
