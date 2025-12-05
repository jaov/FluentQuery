package dev.oveja.jdbc.fluent.interfaces.select;

import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;

public interface SelectBinder<T> {
    SelectMapper<T> bind(ParameterBinder binder);
}
