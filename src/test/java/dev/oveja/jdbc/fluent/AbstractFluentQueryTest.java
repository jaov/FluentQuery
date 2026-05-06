package dev.oveja.jdbc.fluent;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractFluentQueryTest {
    protected static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    protected final ConnectionSupplier supplier = () -> DriverManager.getConnection(URL);

    @BeforeEach
    void setupBase() throws SQLException {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("DROP ALL OBJECTS");
            st.execute("CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), email VARCHAR(255))");
        }
    }
}
