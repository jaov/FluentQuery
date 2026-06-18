package dev.j8a.jdbc.fluent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Default {@link ConnectionSupplier} used when no explicit supplier is provided.
 * <p>
 * This implementation is primarily for the test suite, where the H2 in‑memory
 * database URL is the same as the one defined in {@link AbstractFluentQueryTest}.
 * It returns a new {@link Connection} each time it is called.
 */
public class DefaultConnectionSupplier implements ConnectionSupplier {
    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    @Override
    public Connection get() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
