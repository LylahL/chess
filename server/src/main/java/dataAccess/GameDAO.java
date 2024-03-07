package dataAccess;

import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class GameDAO implements GameDAOInterface{
    private HashSet<GameData> gameData=new HashSet<>();

    public HashSet<GameData> listAllGame(){
        return gameData;
    }

    @Override
    public boolean checkExist(int gameId) {
        return this.getGameByGameId(gameId) != null;
    }

    public GameData getGameByGameId(int gameId) {
        // Get game data based on gameID
        for (GameData game : gameData) {
            if (game.getGameID() == gameId) {
                return game;
            }
        }
        return null;
    }

    public GameData createNewGame(String gameName) {
        // Create and add game data
        GameData game = new GameData(null, null, gameName);
        gameData.add(game);
        return game;
    }


    public void addUserToGame(String clientColor, int gameID, String username) {
        // Add a user to the game data based on the gameID and clientColor
        GameData game=this.getGameByGameId(gameID);
        if (Objects.equals(clientColor, "WHITE")) {
            game.setWhiteUsername(username);
        } else if (Objects.equals(clientColor, "BLACK")) {
            game.setBlackUsername(username);
        }
      else if (Objects.equals(clientColor, null)){
           game.addObserver(username);
      }
    }


    public void clearAllGame() {
        gameData.clear();
    }
}

