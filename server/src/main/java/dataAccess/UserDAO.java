package dataAccess;

import model.UserData;

import java.util.HashSet;
import java.util.Iterator;

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

  public void deleteUser(String username){
    Iterator<UserData> iterator = userData.iterator();
    while (iterator.hasNext()) {
      UserData user = iterator.next();
      if (user.getUsername().equals(username)) {
        // Remove the user if found
        iterator.remove();
        return; // Exit the loop once user is removed
      }
    }
  }

  @Override
  public String getPassword(String username) {
    return this.getUserByUsername(username).getPassword();
  }

  @Override
  public boolean checkExist(String username) {
    return (this.getUserByUsername(username) != null);
  }

}
