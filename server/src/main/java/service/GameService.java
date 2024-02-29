package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class GameService {

  private AuthDAO auth;
  private GameDAO game;
  private UserDAO user;

  public GameService(AuthDAO auth, GameDAO game, UserDAO user) {
    this.auth=auth;
    this.game=game;
    this.user=user;
  }

  //List Games
  public HashSet<GameData> listGames(AuthData authObject) throws ResponseException {
    if (auth.checkExist(authObject)) {
      return game.listAllGame();
    }
    throw new ResponseException(401, "Error: unauthorized");
  }

  //Create Games
  public int createGame(AuthData authObject, String gameName) throws ResponseException {
    if (auth.checkExist(authObject)) {
      game.createNewGame(gameName);
    }else {
      throw new ResponseException(401, "Error: unauthorized");
    }
    return game.getGameIdByName(gameName);
  }
  //Join Game
  public void joinGame(int gameId, String playerColor, AuthData authObject) throws ResponseException{
    if (game.checkExist(gameId)){// if game exist
      if(auth.checkExist(authObject)){// if user logged in
        GameData currentGame = game.getGameByGameId(gameId);
          // already taken
          if (playerColor != null && (playerColor.equals("WHITE") && currentGame.getWhiteUsername() != null )||
                  (Objects.equals(playerColor, "BLACK") && currentGame.getBlackUsername() != null )){
            throw new ResponseException(403, "Error: already taken");
          }
        String username = auth.getUserByAuthToken(authObject);
        game.addUserToGame(playerColor, gameId, username);
      }else{
        // no auth
        throw new ResponseException(401, "Error: unauthorized");
      }
    }else {
      // no game found
      throw new ResponseException(400, "Error: bad request");
    }
  }
}
