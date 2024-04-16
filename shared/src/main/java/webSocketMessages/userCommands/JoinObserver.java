package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{

  public int getGameID() {
    return gameID;
  }

  int gameID;
  public JoinObserver(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
    this.commandType = CommandType.JOIN_OBSERVER;
  }
}
