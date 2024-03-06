package dataAccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDAOInterface {
  void clear() throws ResponseException, DataAccessException;
  String getUserByAuthToken(AuthData authToken) throws DataAccessException;
  AuthData getAuthDataByAuthString(String authToken);
  AuthData createAuthToken(String username);
  void deleteAuthToken(AuthData auth);
  boolean checkExist(AuthData auth);
}
