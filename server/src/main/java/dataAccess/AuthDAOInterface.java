package dataAccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDAOInterface {
  void clear() throws ResponseException, DataAccessException;
  AuthData getAuthDataByAuthString(String authToken);
  AuthData createAuthToken(String username) throws ResponseException, DataAccessException;
  void deleteAuthToken(AuthData auth) throws ResponseException, DataAccessException;
  boolean checkExist(AuthData auth) throws ResponseException;
}
