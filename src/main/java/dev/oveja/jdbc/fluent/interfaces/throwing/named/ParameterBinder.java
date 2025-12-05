package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingConsumer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterBinder extends ThrowingConsumer<PreparedStatement, SQLException> {
}
