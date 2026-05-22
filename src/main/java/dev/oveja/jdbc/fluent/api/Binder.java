package dev.oveja.jdbc.fluent.api;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import dev.oveja.jdbc.fluent.StatementBinder;

public interface Binder<S extends PreparedStatement, R> {
    R bind(StatementBinder<S> binder);
    default R noBind() {
        return bind(s -> {});
    }
}
