package dataAccess;

import exception.ResponseException;
import model.UserData;

public interface UserDAOInterface {
  void clear() throws ResponseException, DataAccessException;
  UserData getUserByUsername(String username);
  void createUser(UserData user) throws ResponseException, DataAccessException;
  String getPassword(String username);

  boolean checkExist(String username);
}
