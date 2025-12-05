package dev.oveja.jdbc.fluent.interfaces.insert.returning.id;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;

public interface InsertIdBinder<T>{
    InsertIdMapper<T> bind(ParameterBinder binder);
}
