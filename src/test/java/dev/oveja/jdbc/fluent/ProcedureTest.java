package dev.oveja.jdbc.fluent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcedureTest extends AbstractFluentQueryTest {

    @BeforeEach
    void setupProcedures() throws SQLException {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            // H2 uses "CREATE ALIAS" for stored procedures
            st.execute("CREATE ALIAS GET_USER_NAME FOR \"dev.oveja.jdbc.fluent.ProcedureTest.getUserName\"");
        }
    }

    /**
     * Helper method for H2 ALIAS. 
     * Must be public static.
     */
    public static String getUserName(String prefix, String suffix) {
        return prefix + " " + suffix;
    }

    @Test
    void testStoredProcedure() throws Exception {
        String result = FluentQuery.forClass(supplier, String.class)
                .call("{? = CALL GET_USER_NAME(?, ?)}")
                .bind(cs -> {
                    cs.registerOutParameter(1, Types.VARCHAR);
                    cs.setString(2, "John");
                    cs.setString(3, "Doe");
                })
                .map(cs -> cs.getString(1))
                .execute();

        assertEquals("John Doe", result);
    }
}
