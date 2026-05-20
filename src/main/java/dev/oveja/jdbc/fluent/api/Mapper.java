package dev.oveja.jdbc.fluent.api;

import dev.oveja.jdbc.fluent.ResultMapper;
import java.sql.SQLException;

public interface Mapper<S, T, R> {
    R map(ResultMapper<S, T> mapper);
}
