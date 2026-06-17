package dev.j8a.jdbc.fluent.api;

import java.util.List;
import java.util.Optional;

public interface QueryMapper<T> {
    Executor<Optional<T>> one();
    ListExecutor<T> list();
}
