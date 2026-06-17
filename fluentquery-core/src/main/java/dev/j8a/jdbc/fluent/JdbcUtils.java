package dev.j8a.jdbc.fluent;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JdbcUtils {

    private JdbcUtils() {
    }

    public static <T> List<T> sqlArrayToList(Array array, Class<T> clazz) throws SQLException {
        if (array == null) {
            return Collections.emptyList();
        }
        Object[] arrayElements = (Object[]) array.getArray();
        if (arrayElements == null) {
            return Collections.emptyList();
        }

        List<T> result = new ArrayList<>(arrayElements.length);
        for (Object element : arrayElements) {
            result.add(clazz.cast(element));
        }
        return result;
    }
}
