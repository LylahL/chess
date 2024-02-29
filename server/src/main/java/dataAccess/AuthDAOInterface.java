package dataAccess;

import model.AuthData;

public interface AuthDAOInterface {
  void clear();
  String getUserByAuthToken(AuthData authToken);
  AuthData getAuthDataByAuthString(String authToken);
  AuthData createAuthToken(String username);
  void deleteAuthToken(AuthData auth);
  boolean checkExist(AuthData auth);
}
