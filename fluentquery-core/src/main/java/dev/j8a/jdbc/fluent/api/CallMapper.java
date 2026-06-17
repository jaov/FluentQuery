package dev.j8a.jdbc.fluent.api;

import dev.j8a.jdbc.fluent.CallableMapper;

public interface CallMapper<T> {
    QueryExecutor<T> map(CallableMapper<T> mapper);
    QueryExecutor<Void> voidCall();
}
