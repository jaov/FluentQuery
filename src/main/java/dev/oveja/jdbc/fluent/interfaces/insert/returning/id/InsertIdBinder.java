package dev.oveja.jdbc.fluent.interfaces.insert.returning.id;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;

public interface InsertIdBinder<T>{
    InsertIdExecutor<T> bind(ParameterBinder binder);

    default InsertIdExecutor<T> noBind() {
        return bind(ps->{});
    }

}
