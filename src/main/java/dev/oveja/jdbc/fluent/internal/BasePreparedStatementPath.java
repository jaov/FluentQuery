package dev.oveja.jdbc.fluent.internal;

import dev.oveja.jdbc.fluent.ThrowingConsumer;
import dev.oveja.jdbc.fluent.api.FluentBinder;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BasePreparedStatementPath<B> implements FluentBinder<B> {

    protected final List<ThrowingConsumer<PreparedStatement, SQLException>> binders = new ArrayList<>();
    protected int currentIndex = 1;

    protected abstract B self();

    @Override
    public B bind(ThrowingConsumer<PreparedStatement, SQLException> binder) {
        binders.add(binder);
        return self();
    }

    @Override
    public B bind(String value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setString(idx, value));
    }

    @Override
    public B bind(int value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setInt(idx, value));
    }

    @Override
    public B bind(long value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setLong(idx, value));
    }

    @Override
    public B bind(double value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setDouble(idx, value));
    }

    @Override
    public B bind(boolean value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBoolean(idx, value));
    }

    @Override
    public B bind(float value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setFloat(idx, value));
    }

    @Override
    public B bind(short value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setShort(idx, value));
    }

    @Override
    public B bind(byte value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setByte(idx, value));
    }

    @Override
    public B bind(byte[] value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBytes(idx, value));
    }

    @Override
    public B bind(BigDecimal value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBigDecimal(idx, value));
    }

    @Override
    public B bind(Date value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setDate(idx, value));
    }

    @Override
    public B bind(Time value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setTime(idx, value));
    }

    @Override
    public B bind(Timestamp value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setTimestamp(idx, value));
    }

    @Override
    public B bind(LocalDate value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setDate(idx, value == null ? null : Date.valueOf(value)));
    }

    @Override
    public B bind(LocalTime value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setTime(idx, value == null ? null : Time.valueOf(value)));
    }

    @Override
    public B bind(LocalDateTime value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setTimestamp(idx, value == null ? null : Timestamp.valueOf(value)));
    }

    @Override
    public B bind(OffsetDateTime value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value));
    }

    @Override
    public B bind(OffsetTime value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value));
    }

    @Override
    public B bind(ZonedDateTime value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value));
    }

    @Override
    public B bind(URL value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setURL(idx, value));
    }

    @Override
    public B bind(Blob value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBlob(idx, value));
    }

    @Override
    public B bind(Clob value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setClob(idx, value));
    }

    @Override
    public B bind(NClob value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setNClob(idx, value));
    }

    @Override
    public B bind(SQLXML value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setSQLXML(idx, value));
    }

    @Override
    public B bind(Ref value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setRef(idx, value));
    }

    @Override
    public B bind(RowId value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setRowId(idx, value));
    }

    @Override
    public B bind(InputStream value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBinaryStream(idx, value));
    }

    @Override
    public B bind(InputStream value, int length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBinaryStream(idx, value, length));
    }

    @Override
    public B bind(InputStream value, long length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setBinaryStream(idx, value, length));
    }

    @Override
    public B bind(Reader value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setCharacterStream(idx, value));
    }

    @Override
    public B bind(Reader value, int length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setCharacterStream(idx, value, length));
    }

    @Override
    public B bind(Reader value, long length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setCharacterStream(idx, value, length));
    }

    @Override
    public B bindNString(String value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setNString(idx, value));
    }

    @Override
    public B bindNCharacterStream(Reader value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setNCharacterStream(idx, value));
    }

    @Override
    public B bindNCharacterStream(Reader value, long length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setNCharacterStream(idx, value, length));
    }

    @Override
    public B bindAsciiStream(InputStream value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setAsciiStream(idx, value));
    }

    @Override
    public B bindAsciiStream(InputStream value, int length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setAsciiStream(idx, value, length));
    }

    @Override
    public B bindAsciiStream(InputStream value, long length) {
        int idx = currentIndex++;
        return bind(ps -> ps.setAsciiStream(idx, value, length));
    }

    @Override
    public B bind(Object value) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value));
    }

    @Override
    public B bind(Object value, int targetSqlType) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value, targetSqlType));
    }

    @Override
    public B bind(Object value, int targetSqlType, int scaleOrLength) {
        int idx = currentIndex++;
        return bind(ps -> ps.setObject(idx, value, targetSqlType, scaleOrLength));
    }

    @Override
    public B bind(Object[] value) {
        int idx = currentIndex++;
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
    public B bind(Collection<?> value) {
        int idx = currentIndex++;
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

    protected String guessSqlTypeName(Class<?> componentType) {
        if (componentType == String.class) return "VARCHAR";
        if (componentType == Integer.class || componentType == int.class) return "INTEGER";
        if (componentType == Long.class || componentType == long.class) return "BIGINT";
        if (componentType == Double.class || componentType == double.class) return "DOUBLE PRECISION";
        if (componentType == Float.class || componentType == float.class) return "REAL";
        if (componentType == Boolean.class || componentType == boolean.class) return "BOOLEAN";
        if (componentType == BigDecimal.class) return "NUMERIC";
        if (componentType == java.sql.Date.class || componentType == LocalDate.class) return "DATE";
        if (componentType == java.sql.Time.class || componentType == LocalTime.class) return "TIME";
        if (componentType == java.sql.Timestamp.class || componentType == LocalDateTime.class) return "TIMESTAMP";
        return "VARCHAR"; // Default fallback
    }

    protected void applyBinders(PreparedStatement ps) throws SQLException {
        for (ThrowingConsumer<PreparedStatement, SQLException> binder : binders) {
            binder.accept(ps);
        }
    }
}
