package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ThrowingFunction;
import java.sql.SQLException;

public interface Mapper<S, T, R> {
    R map(ThrowingFunction<S, T, SQLException> mapper);
}
