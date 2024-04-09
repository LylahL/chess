package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
  private final ChessMove chessMove;

  private final int gameID;

  public MakeMove(String authToken, int gameID, ChessMove chessMove) {
    super(authToken);
    this.chessMove=chessMove;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }
  public ChessMove getChessMove() {
    return chessMove;
  }

}
