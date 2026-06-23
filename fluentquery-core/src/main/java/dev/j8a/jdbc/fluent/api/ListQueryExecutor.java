package dev.j8a.jdbc.fluent.api;

import java.util.List;
import java.util.function.Consumer;

public interface ListQueryExecutor<T> extends QueryExecutor<List<T>> {
    @Override
    ListQueryExecutor<T> log(Consumer<QueryContext> logConsumer);
}

