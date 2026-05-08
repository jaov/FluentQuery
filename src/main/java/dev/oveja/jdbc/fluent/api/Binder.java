package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ThrowingConsumer;
import java.sql.SQLException;

public interface Binder<S, R> {
    R bind(ThrowingConsumer<S, SQLException> binder);
    default R noBind() {
        return bind(s -> {});
    }
}
