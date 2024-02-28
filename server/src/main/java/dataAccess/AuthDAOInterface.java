package dataAccess;

import model.AuthData;

public interface AuthDAOInterface {
  void clear();
  AuthData getAuthByUsername(String username);
  String getUserByAuthToken(AuthData authToken);
  String getUserByAuthString(String authToken);
  AuthData getAuthDataByAuthString(String authToken);
  AuthData createAuthToken(String username);
  void deleteAuthToken(AuthData auth);
  boolean checkExist(AuthData auth);
}
