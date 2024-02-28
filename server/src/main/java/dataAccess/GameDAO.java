package dataAccess;

import exception.ResponseException;
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

    public GameData getGameByUsername(String username) {
        // Get game data based on username, checking both white and black usernames
        for (GameData data : gameData) {
            if (data.getWhiteUsername().equals(username) || data.getBlackUsername().equals(username)) {
                return data;
            }
        }
        return null;
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

    @Override
    public int getGameIdByName(String gameName) throws ResponseException {
        for (GameData game : gameData) {
            if (Objects.equals(game.getGameName(), gameName)) {
                return game.getGameID();
            }
        }
        throw new ResponseException(400, "Error: Game doesn't exist");
    }

    public void createNewGame(String gameName) {
        // Create and add game data
        gameData.add(new GameData(null, null,gameName));
    }


    public void addUserToGame(String clientColor, int gameID, String username) {
        // Add a user to the game data based on the gameID and clientColor
        GameData game=this.getGameByGameId(gameID);
        if (Objects.equals(clientColor, "WHITE")) {
            game.setWhiteUsername(username);
        } else {
            game.setBlackUsername(username);
        }
    }


    public void clearAllGame() {
        gameData.clear();
    }
}

