package dev.oveja.jdbc.fluent.interfaces;

import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertStatementBinder;
import dev.oveja.jdbc.fluent.interfaces.select.SelectBinder;

public interface GenericFlow <T>{
    SelectBinder<T> select(String sql);

    InsertStatementBinder<T> insertReturning(String sql);

}
