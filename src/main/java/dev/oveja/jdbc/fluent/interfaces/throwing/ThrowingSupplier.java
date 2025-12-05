package dev.oveja.jdbc.fluent.interfaces.throwing;

import java.sql.SQLException;

public interface ThrowingSupplier <S,E extends Throwable>{
    S get() throws SQLException;
}
