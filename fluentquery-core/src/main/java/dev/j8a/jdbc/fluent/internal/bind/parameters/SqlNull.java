package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.util.Objects;

public final class SqlNull {
    private final Class<?> type;

    private SqlNull(Class<?> type) {
        this.type = Objects.requireNonNull(type, "SQL Null type token cannot be null.");
    }

    public static SqlNull of(Class<?> type) {
        return new SqlNull(type);
    }

    public Class<?> getType() { return type; }
}
