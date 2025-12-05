package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.insert.returning.id.InsertIdMapper;
import dev.oveja.jdbc.fluent.interfaces.insert.returning.id.InsertIdBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertReturningInsertInsertIdPath<S extends Serializable> implements InsertIdBinder<S>, InsertIdMapper<S> {
    private final String sql;
    private ParameterBinder binder;
    private final RowMapper<S> mapper;

    public InsertReturningInsertInsertIdPath(Class<S> clazz, RowMapper<S> mapper, String sql) {
        this.sql = sql;
        this.mapper= mapper;
    }

    @Override
    public List<S> executeReturningIds() throws SQLException {
        try(Connection con = ConnectionSupplierLoader.load().get();
            PreparedStatement stmt = con.prepareStatement(this.sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            binder.accept(stmt);

            List<S> ret = new ArrayList<>();
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    ret.add(mapper.apply(rs));
                }
                return ret;
            }
        }
    }

    @Override
    public InsertIdMapper<S> bind(ParameterBinder binder) {
        this.binder = binder;
        return this;
    }
}
