package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterDirection;
import dev.j8a.jdbc.fluent.internal.bind.parameters.ParameterKey;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class PsParameterMapCreatorCreatorTest {
    @Test
    void testPipelineCreation_ExposesTransparentSteps() {
        // 1. Arrange: Map input values
        Map<ParameterKey, Object> parameterMap = new TreeMap<>();
        parameterMap.put(new ParameterKey(1, String.class, ParameterDirection.IN), "Jesuso");
        parameterMap.put(new ParameterKey(2, Integer.class, ParameterDirection.IN), 42);

        // 2. Act: Generate the internal pipeline steps instead of the blind lambda wrapper
        List<PsBinderCreator.CompiledStep> pipeline = PsBinderCreator.createPipeline(parameterMap);

        // 3. Assert: Direct field verification
        assertEquals(2, pipeline.size());

        // Inspect Step 1
        PsBinderCreator.CompiledStep step1 = pipeline.get(0);
        assertEquals(1, step1.index);
        assertEquals("Jesuso", step1.value);
        assertEquals(java.sql.Types.VARCHAR, step1.domainType.getJdbcTypeConstant());

        // Inspect Step 2
        PsBinderCreator.CompiledStep step2 = pipeline.get(1);
        assertEquals(2, step2.index);
        assertEquals(42, step2.value);
        assertEquals(java.sql.Types.INTEGER, step2.domainType.getJdbcTypeConstant());
    }
}