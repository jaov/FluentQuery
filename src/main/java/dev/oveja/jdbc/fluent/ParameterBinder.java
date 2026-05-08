package dev.oveja.jdbc.fluent;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterBinder extends ThrowingConsumer<PreparedStatement, SQLException> {
}
