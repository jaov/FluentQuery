package dev.j8a.jdbc.fluent;

import java.sql.*;


import dev.j8a.jdbc.fluent.ConnectionSupplier;
import org.junit.jupiter.api.BeforeEach;

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

    protected void setupH2Returning() throws SQLException {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("CREATE ALIAS IF NOT EXISTS INSERT_RETURNING FOR \"dev.j8a.jdbc.fluent.AbstractFluentQueryTest.insertReturning\"");
        }
    }

    public static ResultSet insertReturning(Connection conn, String name, String email) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, email) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.executeUpdate();
        return ps.getGeneratedKeys();
    }
}
