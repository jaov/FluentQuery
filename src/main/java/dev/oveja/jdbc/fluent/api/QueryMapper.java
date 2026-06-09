package dev.oveja.jdbc.fluent.api;

import java.util.List;
import java.util.Optional;

public interface QueryMapper<T> {
    Executor<Optional<T>> one();
    ListExecutor<T> list();
}
