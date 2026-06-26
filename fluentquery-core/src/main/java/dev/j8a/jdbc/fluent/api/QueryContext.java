package dev.j8a.jdbc.fluent.api;

import java.util.Map;
import java.util.TreeMap;

public final class QueryContext {
    private final String sql;
    private final Map<Integer, Object> boundParameters;
    private final String statementRepresentation;
    private final long executionTimeNanos;

    public QueryContext(String sql, Map<Integer, Object> boundParameters, String statementRepresentation, long executionTimeNanos) {
        this.sql = sql;
        this.boundParameters =  new TreeMap<>(boundParameters);
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
}
