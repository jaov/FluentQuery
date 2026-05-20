package dev.oveja.jdbc.fluent;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultMapper<S, T> {
    T map(S source) throws SQLException;
}
