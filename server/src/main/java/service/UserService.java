package service;

import dataAccess.AuthDAO;
import dataAccess.AuthDAOInterface;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

public class UserService {
  private AuthDAOInterface auth;
  private UserDAOInterface user;

  public UserService(AuthDAOInterface auth, UserDAOInterface user) {
    this.auth=auth;
    this.user=user;
  }
  public AuthData register(UserData newUserData) throws ResponseException {
    String userName =newUserData.getUsername();
    if(user.checkExist(userName)){
      user.createUser(newUserData);
      AuthData authToken = auth.createAuthToken(userName);
      return 
    }
  }
  public AuthData login(UserData user) {}
  public void logout(UserData user) {}
}
