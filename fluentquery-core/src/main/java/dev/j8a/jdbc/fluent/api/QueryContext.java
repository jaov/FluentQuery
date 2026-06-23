package dev.j8a.jdbc.fluent.api;

import java.util.Map;

public final class QueryContext {
    private final String sql;
    private final Map<Integer, Object> boundParameters;
    private final String statementRepresentation;
    private final long executionTimeNanos;

    public QueryContext(String sql, Map<Integer, Object> boundParameters, String statementRepresentation, long executionTimeNanos) {
        this.sql = sql;
        this.boundParameters = Map.copyOf(boundParameters);
        this.statementRepresentation = statementRepresentation;
        this.executionTimeNanos = executionTimeNanos;
    }

    public String getSql() {
        return sql;
    }

    public Map<Integer, Object> getBoundParameters() {
        return boundParameters;
    }

    public String getStatementRepresentation() {
        return statementRepresentation;
    }

    public long getExecutionTimeNanos() {
        return executionTimeNanos;
    }
}
