package service;

import dataAccess.AuthDAOInterface;
import dataAccess.DataAccessException;
import dataAccess.UserDAOInterface;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {
  private AuthDAOInterface auth;
  private UserDAOInterface user;

  public UserService(AuthDAOInterface auth, UserDAOInterface user) {
    this.auth=auth;
    this.user=user;
  }
  public AuthData register(UserData newUserData) throws ResponseException, DataAccessException {
    // just putting stuff in the data base not logging in
    String userName = newUserData.getUsername();
    String password = newUserData.getPassword();
    if(!user.checkExist(userName)){
      if(userName == null || password == null){
        throw new ResponseException(400, "Error: bad request");
      }
      UserData userData = user.createUser(newUserData);
      return login(userData);
    }else {
      throw new ResponseException(403, "Error: Username already taken");
    }
  }
  public AuthData login(UserData userObject) throws ResponseException{
    String username = userObject.getUsername();
    String password = userObject.getPassword();
    if(user.getUserByUsername(username) == null){
      throw new ResponseException(401, "Error: user doesn't exist, please register first");
    }
    // success
    else if(Objects.equals(password, user.getPassword(username))){
      AuthData authToken =null;
      try {
        authToken=auth.createAuthToken(username);
      } catch (DataAccessException e) {
        throw new RuntimeException(e);
      }
      return authToken;
    }else {
      // wrong password
      throw new ResponseException(401, "Error: wrong password");
    }
  }
  public void logout(AuthData authObject) throws ResponseException{
    if(authObject == null) {
      throw  new ResponseException(401, "Error: authObject is null");
    }
    String username = authObject.getUsername();
    if(user.checkExist(username)){
      if(auth.checkExist(authObject)){
        try {
          auth.deleteAuthToken(authObject);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
      }else {
        // user not logged in
        throw new ResponseException(500, "Error: user not logged in");
      }
    }
    else {
      // user not found in system
      throw new ResponseException(401, "Error: User not found in the system");
    }



  }
}
