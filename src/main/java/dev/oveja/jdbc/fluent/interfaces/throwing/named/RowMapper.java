package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingFunction;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> extends ThrowingFunction<ResultSet, T, SQLException> {
}
