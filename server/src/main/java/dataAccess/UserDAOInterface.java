package dataAccess;

import model.UserData;

public interface UserDAOInterface {
  void clear();
  UserData getUserByUsername(String username);
  void createUser(UserData user);
  String getPassword(String username);

  boolean checkExist(String username);
}
