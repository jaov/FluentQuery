package dev.oveja.jdbc.fluent.paths;

import dev.oveja.jdbc.fluent.interfaces.select.SelectBinder;
import dev.oveja.jdbc.fluent.interfaces.select.SelectMapper;
import dev.oveja.jdbc.fluent.interfaces.select.SelectExecute;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.ParameterBinder;
import dev.oveja.jdbc.fluent.interfaces.throwing.named.RowMapper;
import dev.oveja.jdbc.fluent.loader.ConnectionSupplierLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

public class SelectPath<T> implements SelectBinder<T>, SelectMapper<T>, SelectExecute<T> {
    private final String sql;
    private ParameterBinder binder;
    private RowMapper<T> mapper;

    public SelectPath(Class<T> clazz, String sql) {
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
        List<T> returnList = new ArrayList<>();
        try(Connection con = ConnectionSupplierLoader.load().get();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            binder.accept(stmt);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    returnList.add(mapper.apply(rs));
                }
                return returnList;
            }
        }
    }
}
