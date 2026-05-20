package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.StatementBinder;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Binder<S extends PreparedStatement, R> {
    R bind(StatementBinder<S> binder);
    default R noBind() {
        return bind(s -> {});
    }
}
