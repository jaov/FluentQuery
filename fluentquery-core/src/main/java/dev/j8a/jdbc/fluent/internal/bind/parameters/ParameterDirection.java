package dev.j8a.jdbc.fluent.internal.bind.parameters;

import dev.j8a.jdbc.fluent.internal.bind.ProcedureExecutionStep;
import dev.j8a.jdbc.fluent.internal.bind.SqlDomainType;

public enum ParameterDirection {
    IN {
        @Override
        public ProcedureExecutionStep createStep(int idx, int sqlType, SqlDomainType domainType, Object val) {
            return cs -> {
                if (val == null) {
                    cs.setNull(idx, sqlType);
                } else {
                    domainType.getSetter().set(cs, idx, val);
                }
            };
        }
    },

    OUT {
        @Override
        public ProcedureExecutionStep createStep(int idx, int sqlType, SqlDomainType domainType, Object val) {
            return cs -> cs.registerOutParameter(idx, sqlType);
        }
    },

    INOUT {
        @Override
        public ProcedureExecutionStep createStep(int idx, int sqlType, SqlDomainType domainType, Object val) {
            if (val == null) {
                throw new IllegalStateException("INOUT parameter at index " + idx + " cannot have a null payload.");
            }
            return cs -> {
                cs.registerOutParameter(idx, sqlType);
                domainType.getSetter().set(cs, idx, val);
            };
        }
    };

    public abstract ProcedureExecutionStep createStep(int idx, int sqlType, SqlDomainType domainType, Object val);
}
