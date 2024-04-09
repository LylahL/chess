package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{
  private final String message;
  public Notification(ServerMessageType type, String message) {
    super(type);
    this.serverMessageType = ServerMessageType.NOTIFICATION;
    this.message = message;
  }

  public String getMessage() { return message;}
}
