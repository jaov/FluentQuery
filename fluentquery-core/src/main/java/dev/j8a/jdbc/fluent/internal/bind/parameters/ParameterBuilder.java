package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterBuilder {
    private final Map<ParameterKey, Object> parameterMap = new LinkedHashMap<>();
    private int currentIndex = 1;

    // Core Processing Gateway
    public ParameterBuilder bind(Object[] values) {
        for (Object val : values) {
            int idx = currentIndex++;

            if (val == null) {
                throw new IllegalArgumentException(
                    "Raw null detected at position " + (idx - 1) + ". Use SqlNull.of(Class) instead."
                );
            }

            if (val instanceof OutParam) {
                parameterMap.put(new ParameterKey(idx, ((OutParam) val).getType(), ParameterDirection.OUT), null);
            } else if (val instanceof InOutParam) {
                Object innerVal = ((InOutParam) val).getValue();
                parameterMap.put(new ParameterKey(idx, innerVal.getClass(), ParameterDirection.INOUT), innerVal);
            } else if (val instanceof SqlNull) {
                parameterMap.put(new ParameterKey(idx, ((SqlNull) val).getType(), ParameterDirection.IN), null);
            } else {
                // Primitives are wrapped via compile-time Autoboxing before entry
                parameterMap.put(new ParameterKey(idx, val.getClass(), ParameterDirection.IN), val);
            }
        }
        return this;
    }

    // Varargs Ergonomic Facade
    public ParameterBuilder bind(Object first, Object... rest) {
        Object[] combined = new Object[rest.length + 1];
        combined[0] = first;
        System.arraycopy(rest, 0, combined, 1, rest.length);
        return bind(combined);
    }

    // Literal Null Trap Method
    @Deprecated
    public ParameterBuilder bind(Void literalNullTrap) {
        throw new UnsupportedOperationException(
            "Passing a raw literal 'null' is forbidden. Use SqlNull.of(Class) to provide an explicit database type hint."
        );
    }

    public Map<ParameterKey, Object> build() {
        return Collections.unmodifiableMap(this.parameterMap);
    }
}
