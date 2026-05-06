package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingSupplier;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier extends ThrowingSupplier<Connection,SQLException> {

    default boolean shouldClose() {
        return true;
    }

    static ConnectionSupplier borrowed(Connection con) {
        return new ConnectionSupplier() {
            @Override
            public Connection get() throws SQLException {
                return con;
            }

            @Override
            public boolean shouldClose() {
                return false;
            }
        };
    }
}
