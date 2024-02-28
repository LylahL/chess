package dataAccess;

import model.UserData;

public interface UserDAOInterface {
  UserData getUserByUsername(String username);
  void createUser(UserData user);
  void deleteUser(String username);
}
