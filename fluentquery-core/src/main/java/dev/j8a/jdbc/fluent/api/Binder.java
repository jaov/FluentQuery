package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;


import dev.j8a.jdbc.fluent.internal.bind.StatementBinder;

public interface Binder<S extends PreparedStatement, R> {
    R bind(StatementBinder<S> binder);
    default R noBind() {
        return bind(s -> {});
    }
}
