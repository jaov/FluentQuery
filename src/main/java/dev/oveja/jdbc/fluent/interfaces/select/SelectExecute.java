package dev.oveja.jdbc.fluent.interfaces.select;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface SelectExecute<T>{
    List<T> execute() throws SQLException;

    default List<T> executeOrEmpty() {
        try {
            return execute();
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    default Optional<T> fetchOne() throws SQLException {
        List<T> results =  execute();

        if(results.size() > 1) {
            throw new RuntimeException("Query should only return 1 row and returned " + results.size());
        }

        return results.stream().findFirst();
    }

    default Optional<T> fetchOneOrEmpty() {
        try {
            return fetchOne();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
