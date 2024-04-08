package server;

import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
  void notify(ServerMessage serverMessage);
}
