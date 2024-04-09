package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
  private final ChessGame game;
  public LoadGame(ServerMessageType type, ChessGame game) {
    super(type);
    this.serverMessageType = ServerMessageType.LOAD_GAME;
    this.game = game;
  }

  public ChessGame getGame() {
    return game;
  }
}
