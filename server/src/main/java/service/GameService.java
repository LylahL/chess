package service;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class GameService {

  private AuthDAOInterface auth;
  private GameDAOInterface game;
  private UserDAOInterface user;

  public GameService(AuthDAOInterface auth, GameDAOInterface game, UserDAOInterface user) {
    this.auth=auth;
    this.game=game;
    this.user=user;
  }

  //List Games
  public HashSet<GameData> listGames(AuthData authObject) throws ResponseException, DataAccessException {
    if (auth.checkExist(authObject)) {
      return game.listAllGame();
    }
    throw new ResponseException(401, "Error: unauthorized");
  }

  //Create Games
  public int createGame(AuthData authObject, String gameName) throws ResponseException, DataAccessException {
    if (auth.checkExist(authObject)) {
      GameData gameData = game.createNewGame(gameName);
      return gameData.getGameID();
    }else {
      throw new ResponseException(401, "Error: unauthorized");
    }
  }
  //Join Game
  public void joinGame(int gameId, String playerColor, AuthData authObject) throws ResponseException{
    if (game.checkExist(gameId)){// if game exist
      if(auth.checkExist(authObject)){// if user logged in
        GameData currentGame = game.getGameByGameId(gameId);
          // already taken
          if (playerColor != null && (playerColor.equals("WHITE") && currentGame.getWhiteUsername() != null )||
                  (Objects.equals(playerColor, "BLACK") && currentGame.getBlackUsername() != null )){
            throw new ResponseException(403, "Error: team color already taken");
          }
          if (playerColor == null){
            return;
          }
        String username = authObject.getUsername();
        game.addUserToGame(playerColor, gameId, username);
      }else{
        // no auth
        throw new ResponseException(401, "Error: unauthorized");
      }
    }else {
      // no game found
      throw new ResponseException(400, "Error: game is not found in system");
    }
  }
}
