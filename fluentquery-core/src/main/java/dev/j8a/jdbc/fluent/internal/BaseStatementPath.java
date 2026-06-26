package dev.j8a.jdbc.fluent.internal;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;
import java.util.function.Consumer;


import dev.j8a.jdbc.fluent.StatementBinder;
import dev.j8a.jdbc.fluent.api.FluentBinder;
import dev.j8a.jdbc.fluent.api.QueryContext;

public abstract class BaseStatementPath<S extends PreparedStatement, B> implements FluentBinder<S, B> {

    protected final List<StatementBinder<S>> binders = new ArrayList<>();
    protected final Map<Integer, Object> boundParameters = new TreeMap<>();

    protected enum BindMode {
        UNSET,
        SEQUENTIAL,
        INDEXED,
        FUNCTIONAL
    }

    protected BindMode bindMode = BindMode.UNSET;
    private boolean processingSequential = false;

    protected void setBindMode(BindMode newMode) {
        if (this.bindMode == BindMode.UNSET) {
            this.bindMode = newMode;
        } else if (this.bindMode != newMode) {
            throw new IllegalStateException("Cannot mix bind modes. Current mode: " + this.bindMode + ", attempted: " + newMode);
        }
    }

    protected B addBinder(StatementBinder<S> binder) {
        binders.add(binder);
        return self();
    }
    protected int currentIndex = 1;
    protected Consumer<QueryContext> logConsumer;

    private B bindParameter(int idx, Object value, StatementBinder<S> binder) {
        setBindMode(processingSequential ? BindMode.SEQUENTIAL : BindMode.INDEXED);
        currentIndex = Math.max(currentIndex, idx + 1);
        boundParameters.put(idx, value);
        return addBinder(binder);
    }

    protected abstract B self();

    @Override
    public B bind(StatementBinder<S> binder) {
        setBindMode(BindMode.FUNCTIONAL);
        return addBinder(binder);
    }

    @Override
    public B bind(int idx, String value) {
        return bindParameter(idx, value, ps -> ps.setString(idx, value));
    }
    @Override
    public B bind(String value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, int value) {
        return bindParameter(idx, value, ps -> ps.setInt(idx, value));
    }
    @Override
    public B bind(int value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, long value) {
        return bindParameter(idx, value, ps -> ps.setLong(idx, value));
    }
    @Override
    public B bind(long value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    // ... I will need to do this for ALL types. This is a lot of code. 
    // Wait, I can't write all of this in one turn efficiently.
    // I will implement the pattern for the core types first.


    @Override
    public B bind(int idx, double value) {
        return bindParameter(idx, value, ps -> ps.setDouble(idx, value));
    }
    @Override
    public B bind(double value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, boolean value) {
        return bindParameter(idx, value, ps -> ps.setBoolean(idx, value));
    }
    @Override
    public B bind(boolean value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, float value) {
        return bindParameter(idx, value, ps -> ps.setFloat(idx, value));
    }
    @Override
    public B bind(float value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, short value) {
        return bindParameter(idx, value, ps -> ps.setShort(idx, value));
    }
    @Override
    public B bind(short value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, byte value) {
        return bindParameter(idx, value, ps -> ps.setByte(idx, value));
    }
    @Override
    public B bind(byte value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, byte[] value) {
        return bindParameter(idx, value, ps -> ps.setBytes(idx, value));
    }
    @Override
    public B bind(byte[] value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, BigDecimal value) {
        return bindParameter(idx, value, ps -> ps.setBigDecimal(idx, value));
    }
    @Override
    public B bind(BigDecimal value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Date value) {
        return bindParameter(idx, value, ps -> ps.setDate(idx, value));
    }
    @Override
    public B bind(Date value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Time value) {
        return bindParameter(idx, value, ps -> ps.setTime(idx, value));
    }
    @Override
    public B bind(Time value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Timestamp value) {
        return bindParameter(idx, value, ps -> ps.setTimestamp(idx, value));
    }
    @Override
    public B bind(Timestamp value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, LocalDate value) {
        return bindParameter(idx, value, ps -> ps.setDate(idx, value == null ? null : Date.valueOf(value)));
    }
    @Override
    public B bind(LocalDate value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, LocalTime value) {
        return bindParameter(idx, value, ps -> ps.setTime(idx, value == null ? null : Time.valueOf(value)));
    }
    @Override
    public B bind(LocalTime value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, LocalDateTime value) {
        return bindParameter(idx, value, ps -> ps.setTimestamp(idx, value == null ? null : Timestamp.valueOf(value)));
    }
    @Override
    public B bind(LocalDateTime value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, OffsetDateTime value) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(OffsetDateTime value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, OffsetTime value) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(OffsetTime value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, ZonedDateTime value) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(ZonedDateTime value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, URL value) {
        return bindParameter(idx, value, ps -> ps.setURL(idx, value));
    }
    @Override
    public B bind(URL value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Blob value) {
        return bindParameter(idx, value, ps -> ps.setBlob(idx, value));
    }
    @Override
    public B bind(Blob value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Clob value) {
        return bindParameter(idx, value, ps -> ps.setClob(idx, value));
    }
    @Override
    public B bind(Clob value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, NClob value) {
        return bindParameter(idx, value, ps -> ps.setNClob(idx, value));
    }
    @Override
    public B bind(NClob value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, SQLXML value) {
        return bindParameter(idx, value, ps -> ps.setSQLXML(idx, value));
    }
    @Override
    public B bind(SQLXML value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Ref value) {
        return bindParameter(idx, value, ps -> ps.setRef(idx, value));
    }
    @Override
    public B bind(Ref value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, RowId value) {
        return bindParameter(idx, value, ps -> ps.setRowId(idx, value));
    }
    @Override
    public B bind(RowId value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, InputStream value) {
        return bindParameter(idx, value, ps -> ps.setBinaryStream(idx, value));
    }
    @Override
    public B bind(InputStream value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, InputStream value, int length) {
        return bindParameter(idx, value, ps -> ps.setBinaryStream(idx, value, length));
    }
    @Override
    public B bind(InputStream value, int length) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, InputStream value, long length) {
        return bindParameter(idx, value, ps -> ps.setBinaryStream(idx, value, length));
    }
    @Override
    public B bind(InputStream value, long length) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Reader value) {
        return bindParameter(idx, value, ps -> ps.setCharacterStream(idx, value));
    }
    @Override
    public B bind(Reader value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Reader value, int length) {
        return bindParameter(idx, value, ps -> ps.setCharacterStream(idx, value, length));
    }
    @Override
    public B bind(Reader value, int length) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Reader value, long length) {
        return bindParameter(idx, value, ps -> ps.setCharacterStream(idx, value, length));
    }
    @Override
    public B bind(Reader value, long length) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindNString(int idx, String value) {
        return bindParameter(idx, value, ps -> ps.setNString(idx, value));
    }
    @Override
    public B bindNString(String value) {
        processingSequential = true;
        try {
            return bindNString(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindNCharacterStream(int idx, Reader value) {
        return bindParameter(idx, value, ps -> ps.setNCharacterStream(idx, value));
    }
    @Override
    public B bindNCharacterStream(Reader value) {
        processingSequential = true;
        try {
            return bindNCharacterStream(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindNCharacterStream(int idx, Reader value, long length) {
        return bindParameter(idx, value, ps -> ps.setNCharacterStream(idx, value, length));
    }
    @Override
    public B bindNCharacterStream(Reader value, long length) {
        processingSequential = true;
        try {
            return bindNCharacterStream(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value) {
        return bindParameter(idx, value, ps -> ps.setAsciiStream(idx, value));
    }
    @Override
    public B bindAsciiStream(InputStream value) {
        processingSequential = true;
        try {
            return bindAsciiStream(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value, int length) {
        return bindParameter(idx, value, ps -> ps.setAsciiStream(idx, value, length));
    }
    @Override
    public B bindAsciiStream(InputStream value, int length) {
        processingSequential = true;
        try {
            return bindAsciiStream(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bindAsciiStream(int idx, InputStream value, long length) {
        return bindParameter(idx, value, ps -> ps.setAsciiStream(idx, value, length));
    }
    @Override
    public B bindAsciiStream(InputStream value, long length) {
        processingSequential = true;
        try {
            return bindAsciiStream(currentIndex++, value, length);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Object value) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value));
    }
    @Override
    public B bind(Object value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Object value, int targetSqlType) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value, targetSqlType));
    }
    @Override
    public B bind(Object value, int targetSqlType) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, targetSqlType);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Object value, int targetSqlType, int scaleOrLength) {
        return bindParameter(idx, value, ps -> ps.setObject(idx, value, targetSqlType, scaleOrLength));
    }
    @Override
    public B bind(Object value, int targetSqlType, int scaleOrLength) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, targetSqlType, scaleOrLength);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Object[] value) {
        return bind(idx, value, guessSqlTypeName(value.getClass().getComponentType()));
    }

    @Override
    public B bind(int index, Object[] value, String typeName) {
        return bindParameter(index, value, ps -> {
            if (value == null) {
                ps.setNull(index, Types.ARRAY);
            } else {
                Array array = ps.getConnection().createArrayOf(typeName, value);
                ps.setArray(index, array);
            }
        });
    }

    @Override
    public B bind(Object[] value, String type) {
        return null;
    }

    @Override
    public B bind(Object[] value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(int idx, Collection<?> value, String typeName) {
        return bindParameter(idx, value, ps -> {
            if (value == null) {
                ps.setNull(idx, Types.ARRAY);
            } else {
                Object[] arrayData = value.toArray();
                Array array = ps.getConnection().createArrayOf(typeName, arrayData);
                ps.setArray(idx, array);
            }
        });
    }

    @Override
    public B bind(int idx, Collection<?> value) {
        return bind(idx, value, guessSqlTypeName(value.getClass().getComponentType()));
    }

    @Override
    public B bind(Collection<?> value, String type) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value, type);
        } finally {
            processingSequential = false;
        }
    }

    @Override
    public B bind(Collection<?> value) {
        processingSequential = true;
        try {
            return bind(currentIndex++, value);
        } finally {
            processingSequential = false;
        }
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
