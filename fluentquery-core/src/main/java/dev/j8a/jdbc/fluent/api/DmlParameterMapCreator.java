package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

public interface DmlParameterMapCreator extends
    FluentParameterMapCreator<PreparedStatement, DmlParameterMapCreator>,
        QueryExecutor<Integer> {
    @Override
    DmlParameterMapCreator log(Consumer<QueryContext> logConsumer);
}

