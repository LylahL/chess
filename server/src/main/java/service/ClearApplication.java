package service;

import dataAccess.AuthDAOInterface;
import dataAccess.GameDAOInterface;
import dataAccess.UserDAOInterface;

/*
The Service classes implement the actual functionality of the server.
More specifically, the Service classes implement the logic associated with the web endpoints.
 */
public class ClearApplication {
  private final AuthDAOInterface auth;
  private final GameDAOInterface game;
  private final UserDAOInterface user;

  public ClearApplication(AuthDAOInterface auth, GameDAOInterface game, UserDAOInterface user) {
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

