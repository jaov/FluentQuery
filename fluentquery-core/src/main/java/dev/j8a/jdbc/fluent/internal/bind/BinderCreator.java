package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

import java.sql.PreparedStatement;
import java.util.Map;

public interface BinderCreator <S extends PreparedStatement>{
    StatementBinder<S> create(Map<ParameterKey, Object> parameterMap);

}
