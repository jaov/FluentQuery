package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterSetter;

public final class SqlDomainType {
    private final int jdbcTypeConstant;
    private final ParameterSetter setter;

    public SqlDomainType(int jdbcTypeConstant, ParameterSetter setter) {
        this.jdbcTypeConstant = jdbcTypeConstant;
        this.setter = setter;
    }

    public int getJdbcTypeConstant() { return jdbcTypeConstant; }
    public ParameterSetter getSetter() { return setter; }
}
