package dev.j8a.jdbc.fluent.api;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class QueryContext {
    private final String sql;
    private final Map<Integer, Object> boundParameters;
    private final String statementRepresentation;
    private final long executionTimeNanos;

    public QueryContext(String sql, Map<ParameterKey, Object> boundParameters, String statementRepresentation, long executionTimeNanos) {
        this.sql = sql;
        this.boundParameters =  new TreeMap<>(transformMap(boundParameters));
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

    @Override
    public String toString() {
	return new StringBuilder("QueryContext: {\n")
	    .append("\tsql: ").append(sql).append(",\n")
	    .append("\tstatement: ").append(statementRepresentation).append(",\n")
	    .append("\tboundParams: ").append(boundParameters).append(",\n")
	    .append("\texecutionNanos: ").append(executionTimeNanos).append("\n")
	    .append("}").toString();
    }

    private Map<Integer, Object> transformMap(Map<ParameterKey, Object> originalMap) {
        return originalMap.entrySet().stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey().getIndex(), // New Key
                Map.Entry::getValue                // Value remains the same
            ));
    }
}
