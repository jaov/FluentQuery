package dev.j8a.jdbc.fluent.api;

import java.util.List;
import java.util.Optional;

public interface QueryMapper<T> {
    QueryExecutor<Optional<T>> one();
    ListQueryExecutor<T> list();
}
