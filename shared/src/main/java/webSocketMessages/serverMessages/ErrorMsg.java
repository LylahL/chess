package webSocketMessages.serverMessages;

public class ErrorMsg extends ServerMessage{

  public String getMessage() {
    return errorMessage;
  }

  private final String errorMessage;
  public ErrorMsg(String message) {
    super(ServerMessageType.ERROR);

    this.errorMessage = message;
  }
}

