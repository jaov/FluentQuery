package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface ParameterSetter {
    void set(PreparedStatement ps, int idx, Object val) throws SQLException;
}
