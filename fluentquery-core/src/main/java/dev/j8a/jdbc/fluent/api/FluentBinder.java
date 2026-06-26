package dev.j8a.jdbc.fluent.api;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Collection;

public interface FluentBinder<S extends PreparedStatement, B> extends Binder<S, B> {

    B bind(int index, String value);
    B bind(String value);
    B bind(int index, int value);
    B bind(int value);
    B bind(int index, long value);
    B bind(long value);
    B bind(int index, double value);
    B bind(double value);
    B bind(int index, boolean value);
    B bind(boolean value);
    B bind(int index, float value);
    B bind(float value);
    B bind(int index, short value);
    B bind(short value);
    B bind(int index, byte value);
    B bind(byte value);
    B bind(int index, byte[] value);
    B bind(byte[] value);
    B bind(int index, BigDecimal value);
    B bind(BigDecimal value);

    // Legacy Temporal
    B bind(int index, Date value);
    B bind(Date value);
    B bind(int index, Time value);
    B bind(Time value);
    B bind(int index, Timestamp value);
    B bind(Timestamp value);

    // Modern Temporal
    B bind(int index, LocalDate value);
    B bind(LocalDate value);
    B bind(int index, LocalTime value);
    B bind(LocalTime value);
    B bind(int index, LocalDateTime value);
    B bind(LocalDateTime value);
    B bind(int index, OffsetDateTime value);
    B bind(OffsetDateTime value);
    B bind(int index, OffsetTime value);
    B bind(OffsetTime value);
    B bind(int index, ZonedDateTime value);
    B bind(ZonedDateTime value);

    // Specialized Setters
    B bind(int index, URL value);
    B bind(URL value);
    B bind(int index, Blob value);
    B bind(Blob value);
    B bind(int index, Clob value);
    B bind(Clob value);
    B bind(int index, NClob value);
    B bind(NClob value);
    B bind(int index, SQLXML value);
    B bind(SQLXML value);
    B bind(int index, Ref value);
    B bind(Ref value);
    B bind(int index, RowId value);
    B bind(RowId value);

    // Streams
    B bind(int index, InputStream value);
    B bind(InputStream value);
    B bind(int index, InputStream value, int length);
    B bind(InputStream value, int length);
    B bind(int index, InputStream value, long length);
    B bind(InputStream value, long length);
    B bind(int index, Reader value);
    B bind(Reader value);
    B bind(int index, Reader value, int length);
    B bind(Reader value, int length);
    B bind(int index, Reader value, long length);
    B bind(Reader value, long length);

    // National Character Set
    B bindNString(int index, String value);
    B bindNString(String value);
    B bindNCharacterStream(int index, Reader value);
    B bindNCharacterStream(Reader value);
    B bindNCharacterStream(int index, Reader value, long length);
    B bindNCharacterStream(Reader value, long length);

    // Additional Streams
    B bindAsciiStream(int index, InputStream value);
    B bindAsciiStream(InputStream value);
    B bindAsciiStream(int index, InputStream value, int length);
    B bindAsciiStream(InputStream value, int length);
    B bindAsciiStream(int index, InputStream value, long length);
    B bindAsciiStream(InputStream value, long length);

    // Object variants
    B bind(int index, Object value);
    B bind(Object value);
    B bind(int index, Object value, int targetSqlType);
    B bind(Object value, int targetSqlType);
    B bind(int index, Object value, int targetSqlType, int scaleOrLength);
    B bind(Object value, int targetSqlType, int scaleOrLength);

    // Arrays/Collections
    B bind(int index, Object[] value);
    B bind(Object[] value);
    B bind(int index, Collection<?> value);
    B bind(Collection<?> value);

    B bind(int index, Object[] value, String typeName);
    B bind(Object[] value, String typeName);
    B bind(int index, Collection<?> value, String typeName);
    B bind(Collection<?> value, String typeName);

}
