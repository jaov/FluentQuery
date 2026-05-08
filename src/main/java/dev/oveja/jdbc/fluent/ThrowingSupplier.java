package dev.oveja.jdbc.fluent;

import java.sql.SQLException;

public interface ThrowingSupplier <S,E extends Throwable>{
    S get() throws E;
}
