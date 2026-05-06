package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.select.SelectBinder;
import dev.oveja.jdbc.fluent.interfaces.select.SelectMapper;
import dev.oveja.jdbc.fluent.interfaces.select.SelectExecute;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ConnectionSupplier;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class SelectPath<T> implements SelectBinder<T>, SelectMapper<T>, SelectExecute<T> {
    private final String sql;
    private final ConnectionSupplier supplier;
    private ParameterBinder binder;
    private RowMapper<T> mapper;

    public SelectPath(ConnectionSupplier supplier, Class<T> ignoredClazz, String sql) {
        this.supplier = supplier;
        this.sql = sql;
    }

    @Override
    public SelectMapper<T> bind(ParameterBinder binder) {
        this.binder = binder;
        return this;
    }

    @Override
    public SelectExecute<T> map(RowMapper<T> mapper) {
        this.mapper = mapper;
        return this;
    }


    @Override
    public List<T> execute() throws SQLException {
        return execute(this.supplier);
    }

    @Override
    public List<T> execute(ConnectionSupplier supplier) throws SQLException {
        List<T> returnList = new ArrayList<>();
        Connection con = supplier.get();
        try(PreparedStatement stmt = con.prepareStatement(sql)) {
            binder.accept(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    returnList.add(mapper.apply(rs));
                }
                return returnList;
            }
        } finally {
            if (supplier.shouldClose()) {
                con.close();
            }
        }
    }
}
