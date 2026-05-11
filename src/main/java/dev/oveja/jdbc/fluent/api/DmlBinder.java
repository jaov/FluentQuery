package dev.oveja.jdbc.fluent.api;

import java.sql.PreparedStatement;

public interface DmlBinder extends 
        FluentBinder<PreparedStatement, DmlBinder>, 
        Executor<Integer> {
}
