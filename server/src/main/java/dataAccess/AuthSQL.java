package dataAccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public class AuthSQL implements AuthDAOInterface{
  @Override
  public void clear() throws ResponseException, DataAccessException {
    var statement = "TRUNCATE authData";
    DatabaseManager.executeUpdate(statement);
  }

  @Override
  public String getUserByAuthToken(AuthData authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT ";
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public AuthData getAuthDataByAuthString(String authToken) {
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
}
