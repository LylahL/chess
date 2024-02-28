package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

/*
The Service classes implement the actual functionality of the server.
More specifically, the Service classes implement the logic associated with the web endpoints.
 */
public class ClearApplication {
  private final AuthDAO auth;
  private final GameDAO game;
  private final UserDAO user;

  public ClearApplication(AuthDAO auth, GameDAO game, UserDAO user) {
    this.auth=auth;
    this.game=game;
    this.user=user;
  }

  public void clearDataBase(){
    auth.clear();
    game.clearAllGame();
    user.clear();
  }
}

