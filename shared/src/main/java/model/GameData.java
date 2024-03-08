package model;

// public record GameData (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){}
// cannot use record because record is immutable

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Objects;

public class GameData {
  private int gameID;
  private String whiteUsername;
  private String blackUsername;
  private String gameName;

  private ChessGame game;
  private static ArrayList<String> observers = new ArrayList<>();
  private static int n=0;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameData gameData=(GameData) o;
    return gameID == gameData.gameID && Objects.equals(whiteUsername, gameData.whiteUsername) && Objects.equals(blackUsername, gameData.blackUsername) && Objects.equals(gameName, gameData.gameName) && Objects.equals(game, gameData.game);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
  }

  public GameData(String whiteUsername, String blackUsername, String gameName) {
    this.gameID=n++;
    this.whiteUsername=whiteUsername;
    this.blackUsername=blackUsername;
    this.gameName=gameName;
    ChessGame game=new ChessGame();
  }
  public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    // this one is a setter constructor
    this.gameID=gameID;
    this.whiteUsername=whiteUsername;
    this.blackUsername=blackUsername;
    this.gameName=gameName;
    this.game=game;
  }

  public int getGameID() {
    return gameID;
  }

  public String getWhiteUsername() {
    return whiteUsername;
  }

  public void setWhiteUsername(String whiteUsername) {
    this.whiteUsername=whiteUsername;
  }

  public String getBlackUsername() {
    return blackUsername;
  }

  public void setBlackUsername(String blackUsername) {
    this.blackUsername=blackUsername;
  }

  public String getGameName() {
    return gameName;
  }

  public void addObserver(String username){
      observers.add(username);
  }

}

