package dataAccess;

import model.AuthData;

public interface AuthDAOInterface {
  AuthData getAuthByUsername(String username);
  String getAuthByAuthToken(AuthData authToken);
  AuthData createAuthToken(String username);
  void deleteAuthToken(AuthData auth);
  boolean checkExist(AuthData auth);
}
