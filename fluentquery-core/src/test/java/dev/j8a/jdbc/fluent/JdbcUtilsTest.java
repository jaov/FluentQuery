package dev.j8a.jdbc.fluent;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcUtilsTest {

    @Test
    public void testSqlArrayToListWithData() throws SQLException {
        Object[] data = new Object[]{"a", "b", "c"};
        Array array = new StubArray(data);

        List<String> list = JdbcUtils.sqlArrayToList(array, String.class);

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    public void testSqlArrayToListWithNullArray() throws SQLException {
        List<String> list = JdbcUtils.sqlArrayToList(null, String.class);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testSqlArrayToListWithNullInternalArray() throws SQLException {
        Array array = new StubArray(null);
        List<String> list = JdbcUtils.sqlArrayToList(array, String.class);
        assertTrue(list.isEmpty());
    }

    private static class StubArray implements Array {
        private final Object[] data;

        public StubArray(Object[] data) {
            this.data = data;
        }

        @Override
        public String getBaseTypeName() throws SQLException { return null; }
        @Override
        public int getBaseType() throws SQLException { return 0; }
        @Override
        public Object getArray() throws SQLException { return data; }
        @Override
        public Object getArray(Map<String, Class<?>> map) throws SQLException { return null; }
        @Override
        public Object getArray(long index, int count) throws SQLException { return null; }
        @Override
        public Object getArray(long index, int count, Map<String, Class<?>> map) throws SQLException { return null; }
        @Override
        public ResultSet getResultSet() throws SQLException { return null; }
        @Override
        public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException { return null; }
        @Override
        public ResultSet getResultSet(long index, int count) throws SQLException { return null; }
        @Override
        public ResultSet getResultSet(long index, int count, Map<String, Class<?>> map) throws SQLException { return null; }
        @Override
        public void free() throws SQLException {}
    }
}
