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
            st.execute("CREATE ALIAS GET_VERSION FOR \"dev.oveja.jdbc.fluent.ProcedureTest.getVersion\"");
            st.execute("CREATE ALIAS LOG_MESSAGE FOR \"dev.oveja.jdbc.fluent.ProcedureTest.logMessage\"");
            st.execute("CREATE ALIAS TICK FOR \"dev.oveja.jdbc.fluent.ProcedureTest.tick\"");
        }
    }

    public static String getUserName(String prefix, String suffix) {
        return prefix + " " + suffix;
    }

    public static String getVersion() {
        return "1.0.0";
    }

    public static void logMessage(String msg) {
        System.out.println("Stored Proc Log: " + msg);
    }

    public static void tick() {
        System.out.println("Stored Proc Tick");
    }

    @Test
    void testStoredProcedure() throws Exception {
        String result = FluentQuery.forClass(supplier, String.class)
                .call("{? = CALL GET_USER_NAME(?, ?)}")
                .bindOut(Types.VARCHAR) // index 1
                .bind("John")           // index 2
                .bind("Doe")            // index 3
                .map(cs -> cs.getString(1))
                .execute();

        assertEquals("John Doe", result);
    }

    @Test
    void testNoParamsWithMapper() throws Exception {
        String version = FluentQuery.forClass(supplier, String.class)
                .call("{? = CALL GET_VERSION()}")
                .bindOut(Types.VARCHAR)
                .noParams()
                .map(cs -> cs.getString(1))
                .execute();

        assertEquals("1.0.0", version);
    }

    @Test
    void testParamsWithVoidCall() throws Exception {
        FluentQuery.forClass(supplier, Void.class)
                .call("CALL LOG_MESSAGE(?)")
                .bind("Hello Procedural World")
                .voidCall()
                .execute();
        // If no exception, it passed
    }

    @Test
    void testNoParamsWithVoidCall() throws Exception {
        FluentQuery.forClass(supplier, Void.class)
                .call("CALL TICK()")
                .noParams()
                .voidCall()
                .execute();
        // If no exception, it passed
    }
}
