package dataAccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSQL implements AuthDAOInterface{


  @Override
  public void clear() throws ResponseException, DataAccessException {
    var statement = "TRUNCATE authdata";
    DatabaseManager.executeUpdate(statement);
  }
  @Override
  public AuthData getAuthDataByAuthString(String authToken) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT authToken, username FROM authdata WHERE authToken = ?";
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


  public AuthData getAuthDataByUsername(String username) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT authToken, username FROM authdata WHERE username = ?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, username);
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

  //"UPDATE gamedata SET whiteUsername = ? WHERE gameID = ?"
  @Override
  public AuthData createAuthToken(String username) throws ResponseException, DataAccessException {
    var statement = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";
    // generate new authToken
    AuthData authData = new AuthData(username);
    DatabaseManager.executeUpdate(statement, authData.getAuthToken(), username);
    return getAuthDataByAuthString(authData.getAuthToken());

  }

  private boolean checkExistUser(String username) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT username FROM userdata WHERE username = ?";
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

  @Override
  public void deleteAuthToken(AuthData auth) throws ResponseException, DataAccessException {
    var statement = "DELETE FROM authdata WHERE authToken=?";
    DatabaseManager.executeUpdate(statement, auth.getAuthToken());
  }

  @Override
  public boolean checkExist(AuthData auth) throws ResponseException {
    if(auth == null){
      return false;
    }
    var authToken = auth.getAuthToken();
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT authToken, username FROM authdata WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()){
            return rs.next();
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private AuthData readAuthData(ResultSet rs) throws SQLException {
    var authToken = rs.getString("authToken");
    var username = rs.getString("username");
    return new AuthData(authToken, username);
  }
}
