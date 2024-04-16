package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
  private final ChessMove move;

  private final int gameID;

  public MakeMove(String authToken, int gameID, ChessMove move) {
    super(authToken);
    this.move=move;
    this.gameID = gameID;
    this.commandType = CommandType.MAKE_MOVE;
  }

  public int getGameID() {
    return gameID;
  }
  public ChessMove getMove() {
    return move;
  }

}
