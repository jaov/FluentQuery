package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterDirection;
import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class PsBinderCreator implements BinderCreator<PreparedStatement> {

    // Package-private representation allows inspection in unit tests
    static final class CompiledStep implements PsParameterBinder {
        final int index;
        final Object value;
        final SqlDomainType domainType;

        CompiledStep(int index, Object value, SqlDomainType domainType) {
            this.index = index;
            this.value = value;
            this.domainType = domainType;
        }

        @Override
        public void bind(PreparedStatement ps) throws java.sql.SQLException {
            if (value == null) {
                ps.setNull(index, domainType.getJdbcTypeConstant());
            } else {
                domainType.getSetter().set(ps, index, value);
            }
        }
    }

    // Expose the raw step pipeline list directly to package-scoped tests
    static List<CompiledStep> createPipeline(Map<ParameterKey, Object> parameterMap) {

        if(parameterMap == null || parameterMap.isEmpty()) { return Collections.emptyList();}

        List<CompiledStep> pipeline = new ArrayList<>(parameterMap.size());

        for (Map.Entry<ParameterKey, Object> entry : parameterMap.entrySet()) {
            ParameterKey key = entry.getKey();
            Object val = entry.getValue();

            if (key.getDirection() != ParameterDirection.IN) {
                throw new IllegalArgumentException(
                    "Standard queries only support IN parameters."
                );
            }

            SqlDomainType domainType = SqlRegistry.get(key.getType());
            if (domainType == null) {
                throw new IllegalStateException("Unsupported type: " + key.getType().getName());
            }

            pipeline.add(new CompiledStep(key.getIndex(), val, domainType));
        }
        return pipeline;
    }

    public PsParameterBinder create(Map<ParameterKey, Object> parameterMap) {
        List<CompiledStep> pipeline = createPipeline(parameterMap);
        return ps -> {
            for (CompiledStep step : pipeline) {
                step.bind(ps);
            }
        };
    }
}
