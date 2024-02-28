package dataAccess;

import model.GameData;

public interface GameDAOInterface {
  GameData getGameByUsername(String username);
  GameData getGameByGameId(int gameId);
  void createNewGame(GameData game);
  void addUserToGame(String clientColor, int gameID, String username);
  void clearAllGame();
}
