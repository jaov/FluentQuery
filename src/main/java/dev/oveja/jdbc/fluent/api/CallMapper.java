package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.CallableMapper;

public interface CallMapper<T> {
    Executor<T> map(CallableMapper<T> mapper);
    Executor<Void> voidCall();
}
