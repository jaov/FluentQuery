package dev.j8a.jdbc.fluent.internal;

import java.sql.*;
import java.util.*;
import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.internal.bind.BinderCreator;
import dev.j8a.jdbc.fluent.api.FluentParameterMapCreator;
import dev.j8a.jdbc.fluent.api.QueryContext;
import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterBuilder;
import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

public abstract class BaseStatementPath<S extends PreparedStatement, B> implements FluentParameterMapCreator<S, B> {

    private Map<ParameterKey, Object> boundParameters;
    protected BinderCreator<S> binderCreator;
    protected Consumer<QueryContext> logConsumer;

    @Override
    public B noParams() {
        this.boundParameters = Collections.emptyMap();
        return self();
    }

    protected Map<ParameterKey, Object> getBoundParameters() {
        if(boundParameters == null) { return Collections.emptyMap(); }
        return Collections.unmodifiableMap(boundParameters);
    }

    protected abstract B self();

    @Override
    public B bind(Object first, Object... rest) {
        Map<ParameterKey, Object> map = new ParameterBuilder().bind(first,rest).build();
        boundParameters = Collections.unmodifiableMap(map);
        return self();
    }

}
