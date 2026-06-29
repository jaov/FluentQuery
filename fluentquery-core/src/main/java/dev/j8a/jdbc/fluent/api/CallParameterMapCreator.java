package dev.j8a.jdbc.fluent.api;

import java.sql.CallableStatement;

public interface CallParameterMapCreator<T> extends FluentParameterMapCreator<CallableStatement, CallMapper<T>>, CallMapper<T> {
}
