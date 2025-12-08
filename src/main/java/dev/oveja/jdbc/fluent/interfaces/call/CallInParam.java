package dev.oveja.jdbc.fluent.interfaces.call;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.CallableStatementBinder;

public interface CallInParam<T> {
    CallOutParam<T> setInParam(CallableStatementBinder binder);

    default CallOutParam<T> noBinder() {
        return setInParam(cs -> {});
    }
}
