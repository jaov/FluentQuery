package dev.oveja.jdbc.fluent.interfaces.insert.returning;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;

public interface InsertStatementBinder<T> {
    InsertRowMapper<T> bind(ParameterBinder bind);

    default InsertRowMapper<T> noBind() {
        return bind(ps->{});
    }

}
