package dev.oveja.jdbc.fluent;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> extends ThrowingFunction<ResultSet, T, SQLException> {
}
