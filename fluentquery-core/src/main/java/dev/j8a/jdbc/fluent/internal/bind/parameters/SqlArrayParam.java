package dev.j8a.jdbc.fluent.internal.bind.parameters;

import java.util.Collection;
import java.util.Objects;

public final class SqlArrayParam {
    private final Collection<?> collection;
    private final String dbTypeName;

    private SqlArrayParam(Collection<?> collection, String dbTypeName) {
        this.collection = Objects.requireNonNull(collection, "Collection cannot be null.");
        this.dbTypeName = Objects.requireNonNull(dbTypeName, "Database type name cannot be null.");
    }

    public static SqlArrayParam of(Collection<?> collection, String dbTypeName) {
        return new SqlArrayParam(collection, dbTypeName);
    }

    public Collection<?> getCollection() { return collection; }
    public String getDbTypeName() { return dbTypeName; }
}
