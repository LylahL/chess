package dataAccess;

import exception.ResponseException;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQL implements UserDAOInterface{
  @Override
  public void clear() throws ResponseException, DataAccessException {
    var statement = "TRUNCATE userdata";
    DatabaseManager.executeUpdate(statement);
  }

  @Override
  public UserData getUserByUsername(String username) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT username, password, email FROM userdata WHERE username=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, username);
        try (var rs = ps.executeQuery()){
          if(rs.next()){
            return readUserData(rs);
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void createUser(UserData user) throws ResponseException, DataAccessException {
    var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?, ?)";
    DatabaseManager.executeUpdate(statement, user.getUsername(), user.getPassword(), user.getEmail());
  }

  @Override
  public String getPassword(String username) {
    UserData userData = getUserByUsername(username);
    return userData.getPassword();
  }

  @Override
  public boolean checkExist(String username) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT username FROM userdata WHERE username=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, username);
        try (var rs = ps.executeQuery()){
          return rs.next();
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  private UserData readUserData(ResultSet rs) throws SQLException {
    var username = rs.getString("username");
    var password = rs.getString("password");
    var email = rs.getString("email");
    return new UserData(username, password, email);
  }
}
