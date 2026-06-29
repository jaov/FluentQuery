package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.util.Objects;

public final class OutParam {
    private final Class<?> type;

    private OutParam(Class<?> type) {
        this.type = Objects.requireNonNull(type, "OUT parameter type token cannot be null.");
    }

    public static OutParam of(Class<?> type) {
        return new OutParam(type);
    }

    public Class<?> getType() { return type; }
}
