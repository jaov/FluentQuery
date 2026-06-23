package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface DmlBinder extends 
        FluentBinder<PreparedStatement, DmlBinder>, 
        QueryExecutor<Integer> {
    @Override
    DmlBinder log(Consumer<QueryContext> logConsumer);
}

