package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ProcedureBinderCreator implements BinderCreator<CallableStatement> {
    public ProcedureParameterBinder create(Map<ParameterKey, Object> parameterMap) {
        List<ProcedureExecutionStep> pipeline = new ArrayList<>(parameterMap.size());

        for (Map.Entry<ParameterKey, Object> entry : parameterMap.entrySet()) {
            ParameterKey key = entry.getKey();
            Object val = entry.getValue();

            SqlDomainType domainType = SqlRegistry.get(key.getType());
            if (domainType == null) {
                throw new IllegalStateException("Unsupported binding type: " + key.getType().getName());
            }

            ProcedureExecutionStep step = key.getDirection().createStep(
                key.getIndex(),
                domainType.getJdbcTypeConstant(),
                domainType,
                val
            );

            pipeline.add(step);
        }

        return cs -> {
            for (ProcedureExecutionStep step : pipeline) {
                step.execute(cs);
            }
        };
    }
}