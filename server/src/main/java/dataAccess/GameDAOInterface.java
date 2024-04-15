package dataAccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;

import java.util.HashSet;

public interface GameDAOInterface {
  GameData getGameByGameId(int gameId);
  GameData createNewGame(String gameName) throws ResponseException, DataAccessException;
  void addUserToGame(String clientColor, int gameID, String username);
  void clearAllGame() throws ResponseException, DataAccessException;
  HashSet<GameData> listAllGame() throws DataAccessException;

  boolean checkExist(int gameId);

  void makeMove(int gameID, ChessGame game) throws ResponseException, DataAccessException;
}
