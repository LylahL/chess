package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

  public String getMessage() {
    return message;
  }

  private final String message;
  public Error(ServerMessageType type, String message) {
    super(type);
    this.serverMessageType = ServerMessageType.ERROR;
    this.message = message;
  }
}
