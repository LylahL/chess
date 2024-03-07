package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;

public class GameSQL implements GameDAOInterface{
  @Override
  public GameData getGameByGameId(int gameId) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gamedata WHERE gamneID=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setInt(1, gameId);
        try (var rs = ps.executeQuery()){
          if(rs.next()){
            return readGameData(rs);
          }
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public GameData createNewGame(String gameName) throws ResponseException, DataAccessException {
    var statement = "INSERT INTO gamedata (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
    ChessGame chessGame = new ChessGame();
    var game = new Gson().toJson(chessGame);
    GameData chessData = new GameData(null, null, gameName);
    DatabaseManager.executeUpdate(statement, chessData.getGameID(), chessData.getWhiteUsername(), chessData.getBlackUsername(), gameName, game);
    return chessData;
  }

  @Override
  public void addUserToGame(String clientColor, int gameID, String username) {
    try {
      GameData game = getGameByGameId(gameID);
      String gameName = game.getGameName();
      var statement = "";
      if(Objects.equals(clientColor, "WHITE")){
        game.setWhiteUsername(username);
        statement = "UPDATE gamedata SET whiteUsername = ? WHERE id = ?";
      }else if(Objects.equals(clientColor, "BLACK")){
        game.setBlackUsername(username);
        statement = "UPDATE gamedata SET blackUsername = ? WHERE id = ?";
      }
      try {
        DatabaseManager.executeUpdate(statement, gameID, username, gameName, new Gson().toJson(game));
      } catch (ResponseException e) {
        throw new RuntimeException(e);
      } catch (DataAccessException e) {
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clearAllGame() throws ResponseException, DataAccessException {
    var statement = "TRUNCATE gamedata";
    DatabaseManager.executeUpdate(statement);
  }

  @Override
  public HashSet<GameData> listAllGame() throws DataAccessException {
    HashSet<GameData> gameList = new HashSet<>();
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT game FROM gamedata";
      try (var ps = conn.prepareStatement(statement)){
        try (var rs = ps.executeQuery()) {
          while (rs.next()){
            gameList.add(new Gson().fromJson(rs.getString("game"), GameData.class));
          }
          return gameList;
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean checkExist(int gameId) {
    try (var conn = DatabaseManager.getConnection()){
      var statement = "SELECT gameID FROM gamedata WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)){
        ps.setInt(1, gameId);
        try (var rs = ps.executeQuery()){
          return rs.next();
        }
      }
    } catch (SQLException | DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private GameData readGameData(ResultSet rs) throws SQLException {
    var gameID = rs.getInt("gameID");
    var whiteUsername = rs.getString("whiteUsername");
    var blackUsername = rs.getString("blackUsername");
    var gameName = rs.getString("gameName");
    var gameJson = rs.getString("game");
    var chessGameObject = new Gson().fromJson(gameJson, ChessGame.class);
    return new GameData(gameID,whiteUsername, blackUsername, gameName,chessGameObject);
  }
}
