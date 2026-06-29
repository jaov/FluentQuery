package dev.j8a.jdbc.fluent.internal.bind.parameters;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ParameterBuilderTest {

    @Test
    void testSequentialBinding_InboundAndAutoboxingDefaults() {
        ParameterBuilder builder = new ParameterBuilder();

        // Act: Bind regular objects and primitive literals
        Map<ParameterKey, Object> map = builder.bind("Jesuso", 42, true).build();

        assertEquals(3, map.size(), "Map should contain exactly 3 parsed entries.");

        // Assert Parameter 1: String instance
        ParameterKey key1 = findKeyByIndex(map, 1);
        assertEquals(String.class, key1.getType());
        assertEquals(ParameterDirection.IN, key1.getDirection());
        assertEquals("Jesuso", map.get(key1));

        // Assert Parameter 2: Primitive 'int' automatically promoted to 'Integer'
        ParameterKey key2 = findKeyByIndex(map, 2);
        assertEquals(Integer.class, key2.getType(), "Primitive int must auto-box to Integer class token.");
        assertEquals(ParameterDirection.IN, key2.getDirection());
        assertEquals(42, map.get(key2));

        // Assert Parameter 3: Primitive 'boolean' automatically promoted to 'Boolean'
        ParameterKey key3 = findKeyByIndex(map, 3);
        assertEquals(Boolean.class, key3.getType());
        assertEquals(ParameterDirection.IN, key3.getDirection());
        assertEquals(true, map.get(key3));
    }

    @Test
    void testProcedureWrappers_MapsDirectionsAndPeelsPayloads() {
        ParameterBuilder builder = new ParameterBuilder();

        // Act: Mix distinct procedure parameters
        Map<ParameterKey, Object> map = builder.bind(
            "Tx_01",
            OutParam.of(Long.class),
            InOutParam.of(1500)
        ).build();

        assertEquals(3, map.size());

        // Parameter 1: Standard Implicit IN
        ParameterKey key1 = findKeyByIndex(map, 1);
        assertEquals(ParameterDirection.IN, key1.getDirection());

        // Parameter 2: Explicit OUT placeholder extraction
        ParameterKey key2 = findKeyByIndex(map, 2);
        assertEquals(Long.class, key2.getType());
        assertEquals(ParameterDirection.OUT, key2.getDirection());
        assertNull(map.get(key2), "OUT parameters must store a null payload placeholder during mapping.");

        // Parameter 3: Explicit INOUT mapping and inner payload peeling
        ParameterKey key3 = findKeyByIndex(map, 3);
        assertEquals(Integer.class, key3.getType());
        assertEquals(ParameterDirection.INOUT, key3.getDirection());
        assertEquals(1500, map.get(key3), "INOUT parameter must unwrap and preserve its internal value payload.");
    }

    @Test
    void testExplicitSqlNull_MapsCorrectTypeContext() {
        ParameterBuilder builder = new ParameterBuilder();

        // Act: Pass an explicit typed null container
        Map<ParameterKey, Object> map = builder.bind(SqlNull.of(Double.class)).build();

        assertEquals(1, map.size());
        ParameterKey key = findKeyByIndex(map, 1);

        assertEquals(Double.class, key.getType(), "SqlNull must preserve the underlying target SQL class type.");
        assertEquals(ParameterDirection.IN, key.getDirection());
        assertNull(map.get(key), "The mapped data entry value must evaluate to a literal null.");
    }

    @Test
    void testRawNullPassing_TriggersImmediateException() {
        ParameterBuilder builder = new ParameterBuilder();

        // Act & Assert: Attempting to sneak an un-typed null reference array must fail immediately
        Object[] maliciousArray = new Object[] { "Valid String", null };

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            builder.bind(maliciousArray);
        });

        assertTrue(ex.getMessage().contains("Raw null detected"), "Error message should warn about raw null usage.");
    }

    // =========================================================================
    // Private Helper Method for High-Readability Assertions
    // =========================================================================
    private ParameterKey findKeyByIndex(Map<ParameterKey, Object> map, int targetIndex) {
        return map.keySet().stream()
            .filter(k -> k.getIndex() == targetIndex)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Expected to find a ParameterKey registered at index position: " + targetIndex));
    }
}
