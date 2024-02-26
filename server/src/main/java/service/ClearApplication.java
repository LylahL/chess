package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import result.ClearResult;


/*
The Service classes implement the actual functionality of the server.
More specifically, the Service classes implement the logic associated with the web endpoints.
 */
public class ClearApplication {
  private AuthDAO authDAO =  new AuthDAO();
  private UserDAO userDAO =  new UserDAO();
  private GameDAO gameDAO = new GameDAO();

  public ClearApplication(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
    this.authDAO = authDAO;
    this.userDAO = userDAO;
    this.gameDAO = gameDAO;
  }

  public ClearResult clearDataBase(){
    ClearResult result;
    try {
      authDAO.clear();
      userDAO.clear();
      gameDAO.clear();
    } catch (DataAccessException e){
      String errorMessage = e.getMessage();
      return new ClearResult(errorMessage, false);
    }
  }
}
