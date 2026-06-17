package dev.j8a.jdbc.fluent.test;

import dev.j8a.jdbc.fluent.RowMapper;
import java.lang.Override;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
  @Override
  public User map(ResultSet rs) throws SQLException {
    Object idObj = rs.getObject("id", int.class);
    if (idObj == null && rs.wasNull()) {
      throw new SQLException("Column 'id' is null but parameter 'id' is not nullable");
    }
    Object usernameObj = rs.getObject("username", java.lang.String.class);
    if (usernameObj == null && rs.wasNull()) {
      throw new SQLException("Column 'username' is null but parameter 'username' is not nullable");
    }
    Object createdAtObj = rs.getObject("created_at", java.time.LocalDateTime.class);
    if (createdAtObj == null && rs.wasNull()) {
      throw new SQLException("Column 'created_at' is null but parameter 'createdAt' is not nullable");
    }
    return new User((int) idObj, (java.lang.String) usernameObj, (java.time.LocalDateTime) createdAtObj);
  }
}
