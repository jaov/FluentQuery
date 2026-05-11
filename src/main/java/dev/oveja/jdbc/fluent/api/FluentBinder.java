package dev.oveja.jdbc.fluent.api;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Collection;

public interface FluentBinder<S extends PreparedStatement, B> extends Binder<S, B> {

    B bind(String value);
    B bind(int value);
    B bind(long value);
    B bind(double value);
    B bind(boolean value);
    B bind(float value);
    B bind(short value);
    B bind(byte value);
    B bind(byte[] value);
    B bind(BigDecimal value);

    // Legacy Temporal
    B bind(Date value);
    B bind(Time value);
    B bind(Timestamp value);

    // Modern Temporal
    B bind(LocalDate value);
    B bind(LocalTime value);
    B bind(LocalDateTime value);
    B bind(OffsetDateTime value);
    B bind(OffsetTime value);
    B bind(ZonedDateTime value);

    // Specialized Setters
    B bind(URL value);
    B bind(Blob value);
    B bind(Clob value);
    B bind(NClob value);
    B bind(SQLXML value);
    B bind(Ref value);
    B bind(RowId value);

    // Streams
    B bind(InputStream value);
    B bind(InputStream value, int length);
    B bind(InputStream value, long length);
    B bind(Reader value);
    B bind(Reader value, int length);
    B bind(Reader value, long length);

    // National Character Set
    B bindNString(String value);
    B bindNCharacterStream(Reader value);
    B bindNCharacterStream(Reader value, long length);

    // Additional Streams
    B bindAsciiStream(InputStream value);
    B bindAsciiStream(InputStream value, int length);
    B bindAsciiStream(InputStream value, long length);

    // Object variants
    B bind(Object value);
    B bind(Object value, int targetSqlType);
    B bind(Object value, int targetSqlType, int scaleOrLength);

    // Arrays/Collections
    B bind(Object[] value);
    B bind(Collection<?> value);
}
