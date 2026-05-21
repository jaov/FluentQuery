package dev.oveja.jdbc.fluent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoBindTest extends AbstractFluentQueryTest {

    @BeforeEach
    void setupData() throws SQLException {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS collection_tags");
            st.execute("DROP TABLE IF EXISTS tags_table");
            st.execute("DROP TABLE IF EXISTS test_users");
            st.execute("CREATE TABLE test_users (id INT PRIMARY KEY, name VARCHAR(100), age INT, active BOOLEAN, created_at TIMESTAMP)");
            st.execute("INSERT INTO test_users VALUES (1, 'John', 25, true, '2026-05-11 10:00:00')");
            st.execute("INSERT INTO test_users VALUES (2, 'Jane', 30, false, '2026-05-11 11:00:00')");
        }
    }

    @Test
    void testAutoBindSelect() throws Exception {
        List<String> names = FluentQuery.forClass(supplier, String.class)
                .select("SELECT name FROM test_users WHERE age > ? AND active = ?")
                .bind(20)      // index 1
                .bind(true)    // index 2
                .map(rs -> rs.getString(1))
                .execute();

        assertEquals(1, names.size());
        assertEquals("John", names.get(0));
    }

    @Test
    void testAutoBindDml() throws Exception {
        int updated = FluentQuery.update(supplier, "UPDATE test_users SET name = ? WHERE id = ?")
                .bind("Johnny") // index 1
                .bind(1)        // index 2
                .execute();

        assertEquals(1, updated);

        String name = FluentQuery.forClass(supplier, String.class)
                .select("SELECT name FROM test_users WHERE id = 1")
                .noBind()
                .mapOne(rs -> rs.getString(1))
                .execute()
                .orElseThrow();

        assertEquals("Johnny", name);
    }

    @Test
    void testAutoBindModernTemporal() throws Exception {
        LocalDateTime now = LocalDateTime.of(2026, 5, 11, 12, 0);
        
        FluentQuery.update(supplier, "UPDATE test_users SET created_at = ? WHERE id = ?")
                .bind(now) // index 1
                .bind(2)   // index 2
                .execute();

        LocalDateTime createdAt = FluentQuery.forClass(supplier, LocalDateTime.class)
                .select("SELECT created_at FROM test_users WHERE id = 2")
                .noBind()
                .mapOne(rs -> rs.getTimestamp(1).toLocalDateTime())
                .execute()
                .orElseThrow();

        assertEquals(now, createdAt);
    }

    @Test
    void testAllModernTemporalTypes() throws Exception {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("DROP TABLE IF EXISTS temporal_test");
            st.execute("CREATE TABLE temporal_test (d DATE, t TIME, ts TIMESTAMP)");
        }

        java.time.LocalDate date = java.time.LocalDate.of(2026, 5, 11);
        java.time.LocalTime time = java.time.LocalTime.of(14, 30, 0);
        java.time.LocalDateTime dateTime = java.time.LocalDateTime.of(2026, 5, 11, 14, 30, 0);

        FluentQuery.insert(supplier, "INSERT INTO temporal_test VALUES (?, ?, ?)")
                .bind(date)     // setDate
                .bind(time)     // setTime
                .bind(dateTime) // setTimestamp
                .execute();

        FluentQuery.forClass(supplier, String.class)
                .select("SELECT d, t, ts FROM temporal_test")
                .noBind()
                .mapOne(rs -> {
                    assertEquals(date, rs.getDate(1).toLocalDate());
                    assertEquals(time, rs.getTime(2).toLocalTime());
                    assertEquals(dateTime, rs.getTimestamp(3).toLocalDateTime());
                    return "OK";
                })
                .execute();
    }

    @Test
    void testArrayBind() throws Exception {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE tags_table (id INT, tags VARCHAR ARRAY)");
        }

        String[] tags = {"java", "sql", "fluent"};
        
        FluentQuery.insert(supplier, "INSERT INTO tags_table VALUES (?, ?)")
                .bind(1)    // index 1
                .bind(tags) // index 2
                .execute();

        Object[] resultTags = (Object[]) FluentQuery.forClass(supplier, Object.class)
                .select("SELECT tags FROM tags_table WHERE id = 1")
                .noBind()
                .mapOne(rs -> rs.getArray(1).getArray())
                .execute()
                .orElseThrow();

        assertEquals(3, resultTags.length);
        assertEquals("java", resultTags[0]);
    }

    @Test
    void testCollectionBind() throws Exception {
        try (Connection con = supplier.get(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE collection_tags (id INT, tags VARCHAR ARRAY)");
        }

        List<String> tags = Arrays.asList("a", "b");
        
        FluentQuery.insert(supplier, "INSERT INTO collection_tags VALUES (?, ?)")
                .bind(2)    // index 1
                .bind(tags) // index 2
                .execute();

        Object[] resultTags = (Object[]) FluentQuery.forClass(supplier, Object.class)
                .select("SELECT tags FROM collection_tags WHERE id = 2")
                .noBind()
                .mapOne(rs -> rs.getArray(1).getArray())
                .execute()
                .orElseThrow();

        assertEquals(2, resultTags.length);
        assertEquals("a", resultTags[0]);
    }
}
