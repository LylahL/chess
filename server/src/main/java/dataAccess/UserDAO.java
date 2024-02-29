package dataAccess;

import model.UserData;

import java.util.HashSet;

public class UserDAO implements UserDAOInterface{
  private HashSet<UserData> userData = new HashSet<>();

  @Override
  public void clear() {
    userData.clear();
  }

  public UserData getUserByUsername(String username) {
    // get userdata base on username
    for(UserData user : userData){
      if(user.getUsername().equals(username)){
        return user;
      }
    }
    return null;
  }

  public void createUser(UserData user) {
    userData.add(user);
  }

  @Override
  public String getPassword(String username) {
    return this.getUserByUsername(username).getPassword();
  }

  @Override
  public boolean checkExist(String username) {
    // if exist then true
    return (this.getUserByUsername(username) != null);
  }

}
