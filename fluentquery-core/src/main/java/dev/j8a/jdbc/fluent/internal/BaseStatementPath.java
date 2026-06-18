package dev.j8a.jdbc.fluent.internal;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import dev.j8a.jdbc.fluent.StatementBinder;
import dev.j8a.jdbc.fluent.api.FluentBinder;

public abstract class BaseStatementPath<S extends PreparedStatement, B> implements FluentBinder<S, B> {

    protected final List<StatementBinder<S>> binders = new ArrayList<>();
    protected int currentIndex = 1;

    protected abstract B self();

    @Override
    public B bind(StatementBinder<S> binder) {
        binders.add(binder);
        return self();
    }

    @Override
    public B bind(int idx, String value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setString(idx, value));
    }
    @Override
    public B bind(String value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, int value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setInt(idx, value));
    }
    @Override
    public B bind(int value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, long value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setLong(idx, value));
    }
    @Override
    public B bind(long value) {
        return bind(currentIndex++, value);
    }

    // ... I will need to do this for ALL types. This is a lot of code. 
    // Wait, I can't write all of this in one turn efficiently.
    // I will implement the pattern for the core types first.


    @Override
    public B bind(int idx, double value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setDouble(idx, value));
    }
    @Override
    public B bind(double value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, boolean value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBoolean(idx, value));
    }
    @Override
    public B bind(boolean value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, float value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setFloat(idx, value));
    }
    @Override
    public B bind(float value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, short value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setShort(idx, value));
    }
    @Override
    public B bind(short value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, byte value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setByte(idx, value));
    }
    @Override
    public B bind(byte value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, byte[] value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBytes(idx, value));
    }
    @Override
    public B bind(byte[] value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, BigDecimal value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBigDecimal(idx, value));
    }
    @Override
    public B bind(BigDecimal value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Date value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setDate(idx, value));
    }
    @Override
    public B bind(Date value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Time value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setTime(idx, value));
    }
    @Override
    public B bind(Time value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Timestamp value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setTimestamp(idx, value));
    }
    @Override
    public B bind(Timestamp value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, LocalDate value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setDate(idx, value == null ? null : Date.valueOf(value)));
    }
    @Override
    public B bind(LocalDate value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, LocalTime value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setTime(idx, value == null ? null : Time.valueOf(value)));
    }
    @Override
    public B bind(LocalTime value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, LocalDateTime value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setTimestamp(idx, value == null ? null : Timestamp.valueOf(value)));
    }
    @Override
    public B bind(LocalDateTime value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, OffsetDateTime value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(OffsetDateTime value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, OffsetTime value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(OffsetTime value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, ZonedDateTime value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(ZonedDateTime value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, URL value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setURL(idx, value));
    }
    @Override
    public B bind(URL value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Blob value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBlob(idx, value));
    }
    @Override
    public B bind(Blob value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Clob value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setClob(idx, value));
    }
    @Override
    public B bind(Clob value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, NClob value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setNClob(idx, value));
    }
    @Override
    public B bind(NClob value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, SQLXML value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setSQLXML(idx, value));
    }
    @Override
    public B bind(SQLXML value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Ref value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setRef(idx, value));
    }
    @Override
    public B bind(Ref value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, RowId value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setRowId(idx, value));
    }
    @Override
    public B bind(RowId value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, InputStream value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBinaryStream(idx, value));
    }
    @Override
    public B bind(InputStream value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, InputStream value, int length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBinaryStream(idx, value, length));
    }
    @Override
    public B bind(InputStream value, int length) {
        return bind(currentIndex++, value, length);
    }

    @Override
    public B bind(int idx, InputStream value, long length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setBinaryStream(idx, value, length));
    }
    @Override
    public B bind(InputStream value, long length) {
        return bind(currentIndex++, value, length);
    }

    @Override
    public B bind(int idx, Reader value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setCharacterStream(idx, value));
    }
    @Override
    public B bind(Reader value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Reader value, int length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setCharacterStream(idx, value, length));
    }
    @Override
    public B bind(Reader value, int length) {
        return bind(currentIndex++, value, length);
    }

    @Override
    public B bind(int idx, Reader value, long length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setCharacterStream(idx, value, length));
    }
    @Override
    public B bind(Reader value, long length) {
        return bind(currentIndex++, value, length);
    }

    @Override
    public B bindNString(int idx, String value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setNString(idx, value));
    }
    @Override
    public B bindNString(String value) {
        return bindNString(currentIndex++, value);
    }

    @Override
    public B bindNCharacterStream(int idx, Reader value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setNCharacterStream(idx, value));
    }
    @Override
    public B bindNCharacterStream(Reader value) {
        return bindNCharacterStream(currentIndex++, value);
    }

    @Override
    public B bindNCharacterStream(int idx, Reader value, long length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setNCharacterStream(idx, value, length));
    }
    @Override
    public B bindNCharacterStream(Reader value, long length) {
        return bindNCharacterStream(currentIndex++, value, length);
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setAsciiStream(idx, value));
    }
    @Override
    public B bindAsciiStream(InputStream value) {
        return bindAsciiStream(currentIndex++, value);
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value, int length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setAsciiStream(idx, value, length));
    }
    @Override
    public B bindAsciiStream(InputStream value, int length) {
        return bindAsciiStream(currentIndex++, value, length);
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value, long length) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setAsciiStream(idx, value, length));
    }
    @Override
    public B bindAsciiStream(InputStream value, long length) {
        return bindAsciiStream(currentIndex++, value, length);
    }

    @Override
    public B bind(int idx, Object value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(Object value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Object value, int targetSqlType) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value, targetSqlType));
    }
    @Override
    public B bind(Object value, int targetSqlType) {
        return bind(currentIndex++, value, targetSqlType);
    }

    @Override
    public B bind(int idx, Object value, int targetSqlType, int scaleOrLength) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> ps.setObject(idx, value, targetSqlType, scaleOrLength));
    }
    @Override
    public B bind(Object value, int targetSqlType, int scaleOrLength) {
        return bind(currentIndex++, value, targetSqlType, scaleOrLength);
    }

    @Override
    public B bind(int idx, Object[] value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> {
            if (value == null) {
                ps.setNull(idx, Types.ARRAY);
            } else {
                String typeName = guessSqlTypeName(value.getClass().getComponentType());
                Array array = ps.getConnection().createArrayOf(typeName, value);
                ps.setArray(idx, array);
            }
        });
    }
    @Override
    public B bind(Object[] value) {
        return bind(currentIndex++, value);
    }

    @Override
    public B bind(int idx, Collection<?> value) {
        currentIndex = Math.max(currentIndex, idx + 1);
        return bind(ps -> {
            if (value == null) {
                ps.setNull(idx, Types.ARRAY);
            } else {
                Object[] arrayData = value.toArray();
                String typeName = guessSqlTypeName(arrayData.getClass().getComponentType());
                Array array = ps.getConnection().createArrayOf(typeName, arrayData);
                ps.setArray(idx, array);
            }
        });
    }
    @Override
    public B bind(Collection<?> value) {
        return bind(currentIndex++, value);
    }

    protected String guessSqlTypeName(Class<?> componentType) {
        if (componentType == String.class) return "VARCHAR";
        if (componentType == Integer.class || componentType == int.class) return "INTEGER";
        if (componentType == Long.class || componentType == long.class) return "BIGINT";
        if (componentType == Double.class || componentType == double.class) return "DOUBLE PRECISION";
        if (componentType == Float.class || componentType == float.class) return "REAL";
        if (componentType == Boolean.class || componentType == boolean.class) return "BOOLEAN";
        if (componentType == BigDecimal.class) return "NUMERIC";
        if (componentType == Date.class || componentType == LocalDate.class) return "DATE";
        if (componentType == Time.class || componentType == LocalTime.class) return "TIME";
        if (componentType == Timestamp.class || componentType == LocalDateTime.class) return "TIMESTAMP";
        return "VARCHAR"; // Default fallback
    }

    protected void applyBinders(S ps) throws SQLException {
        for (StatementBinder<S> binder : binders) {
            binder.bind(ps);
        }
    }
}
