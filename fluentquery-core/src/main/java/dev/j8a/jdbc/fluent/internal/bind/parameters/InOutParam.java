package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.util.Objects;

public final class InOutParam {
    private final Object value;

    private InOutParam(Object value) {
        this.value = Objects.requireNonNull(value, "INOUT parameter baseline payload value cannot be null.");
    }

    public static InOutParam of(Object value) {
        return new InOutParam(value);
    }

    public Object getValue() {
        return value;
    }
}