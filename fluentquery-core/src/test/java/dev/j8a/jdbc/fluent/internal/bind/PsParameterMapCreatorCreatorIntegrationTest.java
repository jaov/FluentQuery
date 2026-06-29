package dev.j8a.jdbc.fluent.internal.bind;

import dev.j8a.jdbc.fluent.AbstractFluentQueryTest;
import dev.j8a.jdbc.fluent.FluentQuery;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class PsParameterMapCreatorCreatorIntegrationTest extends AbstractFluentQueryTest {

    @Test
    void CreatedPsBinderWorks() throws SQLException {
        FluentQuery.insertReturningId("INSERT INTO users (name, email) VALUES (?, ?)")
            .bind("John","john@example.com")             // Explicit index 1
            .mapInt()
            .one()
            .execute(supplier);

    }
}