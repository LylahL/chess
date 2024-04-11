package server;

import com.google.gson.Gson;
import ui.EscapeSequences;
import ui.GameplayUI;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public class NotificationHandler {
  public void notify(String message) {
    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
      Notification notification = new Gson().fromJson(message, Notification.class);
      System.out.println(notification.getMessage());
      }
    else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
    LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
    GameplayUI.drawBoard(loadGame.getGame().getBoard());
  }
}
}
