package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;

public interface DmlBinder extends 
        FluentBinder<PreparedStatement, DmlBinder>, 
        QueryExecutor<Integer> {
}
