package dev.oveja.jdbc.fluent.interfaces.throwing.named;

import dev.oveja.jdbc.fluent.interfaces.throwing.ThrowingSupplier;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ConnectionSupplier extends ThrowingSupplier<Connection,SQLException> {
}
