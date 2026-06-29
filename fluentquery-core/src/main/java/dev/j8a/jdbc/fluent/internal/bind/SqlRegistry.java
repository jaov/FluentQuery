package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.internal.bind.parameters.SqlArrayParam;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SqlRegistry {

    private static final Map<Class<?>, SqlDomainType> DOMAIN_REGISTRY;

    static {
        Map<Class<?>, SqlDomainType> registry = new HashMap<>();

        // ==========================================
        // 1. Text & Character Types
        // ==========================================
        registry.put(String.class, new SqlDomainType(
            java.sql.Types.VARCHAR,
            (ps, idx, val) -> ps.setString(idx, (String) val)
        ));
        registry.put(Character.class, new SqlDomainType(
            java.sql.Types.CHAR,
            (ps, idx, val) -> ps.setString(idx, String.valueOf(val))
        ));

        // ==========================================
        // 2. Numeric Types (Primes and Wrappers handled via Autoboxing)
        // ==========================================
        registry.put(Integer.class, new SqlDomainType(
            java.sql.Types.INTEGER,
            (ps, idx, val) -> ps.setInt(idx, (Integer) val)
        ));
        registry.put(Long.class, new SqlDomainType(
            java.sql.Types.BIGINT,
            (ps, idx, val) -> ps.setLong(idx, (Long) val)
        ));
        registry.put(Short.class, new SqlDomainType(
            java.sql.Types.SMALLINT,
            (ps, idx, val) -> ps.setShort(idx, (Short) val)
        ));
        registry.put(Double.class, new SqlDomainType(
            java.sql.Types.DOUBLE,
            (ps, idx, val) -> ps.setDouble(idx, (Double) val)
        ));
        registry.put(Float.class, new SqlDomainType(
            java.sql.Types.REAL,
            (ps, idx, val) -> ps.setFloat(idx, (Float) val)
        ));
        registry.put(java.math.BigDecimal.class, new SqlDomainType(
            java.sql.Types.NUMERIC,
            (ps, idx, val) -> ps.setBigDecimal(idx, (java.math.BigDecimal) val)
        ));

        // ==========================================
        // 3. State & State Flags
        // ==========================================
        registry.put(Boolean.class, new SqlDomainType(
            java.sql.Types.BOOLEAN,
            (ps, idx, val) -> ps.setBoolean(idx, (Boolean) val)
        ));
        registry.put(byte[].class, new SqlDomainType(
            java.sql.Types.VARBINARY,
            (ps, idx, val) -> ps.setBytes(idx, (byte[]) val)
        ));

        // ==========================================
        // 4. Modern Java 8 Time API
        // ==========================================
        registry.put(LocalDate.class, new SqlDomainType(
            java.sql.Types.DATE,
            (ps, idx, val) -> ps.setDate(idx, java.sql.Date.valueOf((LocalDate) val))
        ));
        registry.put(LocalTime.class, new SqlDomainType(
            java.sql.Types.TIME,
            (ps, idx, val) -> ps.setTime(idx, Time.valueOf((LocalTime) val))
        ));
        registry.put(LocalDateTime.class, new SqlDomainType(
            java.sql.Types.TIMESTAMP,
            (ps, idx, val) -> ps.setTimestamp(idx, Timestamp.valueOf((LocalDateTime) val))
        ));
        registry.put(Instant.class, new SqlDomainType(
            java.sql.Types.TIMESTAMP,
            (ps, idx, val) -> ps.setTimestamp(idx, Timestamp.from((Instant) val))
        ));

        // ==========================================
        // 5. Legacy Temporal Types (For backwards compatibility)
        // ==========================================
        registry.put(java.util.Date.class, new SqlDomainType(
            java.sql.Types.TIMESTAMP,
            (ps, idx, val) -> ps.setTimestamp(idx, new Timestamp(((java.util.Date) val).getTime()))
        ));
        registry.put(java.sql.Date.class, new SqlDomainType(
            java.sql.Types.DATE,
            (ps, idx, val) -> ps.setDate(idx, (java.sql.Date) val)
        ));
        registry.put(Timestamp.class, new SqlDomainType(
            java.sql.Types.TIMESTAMP,
            (ps, idx, val) -> ps.setTimestamp(idx, (Timestamp) val)
        ));

        // ==========================================
        // 6. Arrays
        // ==========================================
        registry.put(SqlArrayParam.class, new SqlDomainType(
            java.sql.Types.ARRAY,
            (ps, idx, val) -> {
                SqlArrayParam arrayParam = (SqlArrayParam) val;

                Object[] elementArray = arrayParam.getCollection().toArray(new Object[0]);
                java.sql.Connection conn = ps.getConnection();

                java.sql.Array sqlArray = conn.createArrayOf(arrayParam.getDbTypeName(), elementArray);
                ps.setArray(idx, sqlArray);
            }
        ));

        DOMAIN_REGISTRY = Collections.unmodifiableMap(registry);
    }

    public static SqlDomainType get(Class<?> type) {
        return DOMAIN_REGISTRY.get(type);
    }
}