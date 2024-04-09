package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
  private final ChessGame.TeamColor playerColor;

  public int getGameID() {
    return gameID;
  }

  private final int gameID;

  public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.playerColor=playerColor;
    this.gameID = gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }
}
