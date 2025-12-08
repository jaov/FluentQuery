package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertExecutor;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertRowMapper;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.InsertStatementBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningPath<T> implements InsertStatementBinder<T>, InsertRowMapper<T>, InsertExecutor<T> {

    private final String sql;
    private ParameterBinder binder;
    private RowMapper<T> mapper;

    public InsertReturningPath(Class<T> ignoredClazz, String sql) {
        this.sql = sql;
    }


    @Override
    public InsertExecutor<T> map(RowMapper<T> mapper) {
        this.mapper = mapper;
        return this;
    }

    @Override
    public InsertRowMapper<T> bind(ParameterBinder binder) {
        this.binder = binder;
        return this;
    }

    @Override
    public List<T> executeReturning() throws SQLException {
        try(Connection con = ConnectionSupplierLoader.load().get();
            PreparedStatement stmt = con.prepareStatement(this.sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            binder.accept(stmt);

            List<T> ret = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    ret.add(mapper.apply(rs));
                }
                return ret;
            }
        }

    }
}
