package dev.j8a.jdbc.fluent.api;

import dev.j8a.jdbc.fluent.CallableMapper;

public interface CallMapper<T> {
    Executor<T> map(CallableMapper<T> mapper);
    Executor<Void> voidCall();
}
