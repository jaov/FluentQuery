package dev.oveja.jdbc.fluent.interfaces.call;

import java.sql.SQLException;

public interface CallOutParam<T> {
    T withOutParam(String outParam) throws SQLException;
}
