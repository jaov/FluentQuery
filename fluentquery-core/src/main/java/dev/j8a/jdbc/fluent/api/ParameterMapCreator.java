package dev.j8a.jdbc.fluent.api;

import java.sql.PreparedStatement;

public interface ParameterMapCreator<R> {
    R bind(Object first, Object... rest);
    R noParams();
}
