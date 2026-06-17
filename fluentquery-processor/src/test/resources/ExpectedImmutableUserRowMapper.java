package dev.j8a.jdbc.fluent.test;

import dev.j8a.jdbc.fluent.RowMapper;
import java.lang.Override;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImmutableUserRowMapper implements RowMapper<ImmutableUser> {
  @Override
  public ImmutableUser map(ResultSet rs) throws SQLException {
    ImmutableImmutableUser.Builder builder = ImmutableImmutableUser.builder();
    builder.id(rs.getObject("id", int.class));
    builder.username(rs.getObject("username", String.class));
    return builder.build();
  }
}
