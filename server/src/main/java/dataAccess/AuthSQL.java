package dataAccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSQL implements AuthDAOInterface{
  @Override
  public void clear() throws ResponseException, DataAccessException {
    var statement = "TRUNCATE authData";
    DatabaseManager.executeUpdate(statement);
  }

  @Override
  public String getUserByAuthToken(AuthData authToken) throws DataAccessException {
    String authTokenString = authToken.getAuthToken();
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT authToken FROM authData WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, authTokenString);
        try (var rs = ps.executeQuery()){
          if(rs.next()){
            var username = rs.getString("username");
            return username;
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public AuthData getAuthDataByAuthString(String authToken) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()){
          if(rs.next()){
            return readAuthData(rs);
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public AuthData createAuthToken(String username) {
    return null;
  }

  @Override
  public void deleteAuthToken(AuthData auth) {

  }

  @Override
  public boolean checkExist(AuthData auth) {
    return false;
  }

  private AuthData readAuthData(ResultSet rs) throws SQLException {
    var authToken = rs.getString("authToken");
    var username = rs.getString("username");
    return new AuthData(authToken, username);
  }
}
