package dataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class GameDAO implements DataAccessInterface{
    private HashSet<GameData> gameData = new HashSet<>();


    public Object getData(String username) {
        // Get game data based on username, checking both white and black usernames
        for (GameData data : gameData) {
            if (data.getWhiteUsername().equals(username) || data.getBlackUsername().equals(username)) {
                return data;
            }
        }
        return null;
    }

    public GameData getData(int gameId) {
        // Get game data based on gameID
        for (GameData game : gameData) {
            if (game.getGameID() == gameId) {
                return game;
            }
        }
        return null;
    }

    public Object createData(GameData game) {
        // Create and add game data
        gameData.add(game);
        return game;
    }


    public void setData(String clientColor, int gameID, String username) {
        // Add a user to the game data based on the gameID and clientColor
        GameData game = getData(gameID);
        if (Objects.equals(clientColor, "WHITE")) {
            game.setWhiteUsername(username);
        }else{
            game.setBlackUsername(username);
        }
    }


    public void deleteData() {
        gameData.clear();
    }


    public Object checkExist(AuthData auth) {
        return null;
    }
}
