package dataAccess;

import exception.ResponseException;
import model.GameData;

import java.util.HashSet;

public interface GameDAOInterface {
  GameData getGameByUsername(String username);
  GameData getGameByGameId(int gameId);
  int getGameIdByName(String gameName) throws ResponseException;
  void createNewGame(String gameName);
  void addUserToGame(String clientColor, int gameID, String username);
  void clearAllGame();
  HashSet<GameData> listAllGame();

  boolean checkExist(int gameId);
}
