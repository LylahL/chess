package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class UserDAO implements DataAccessInterface{
  private HashSet<UserData> userData = new HashSet<>();

  public UserData getData(String username) {
    // get userdata base on username

  }


  public Object getData(AuthData authToken) {
    return null;
  }


  public void createData(UserData user) {
    userData.add(user);
  }


  public Object setData() {
    return null;
  }


  public Object deleteData(AuthData auth) {
    return null;
  }

  public Object checkExist(AuthData auth) {
    return null;
  }
}
