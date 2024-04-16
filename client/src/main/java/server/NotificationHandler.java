package server;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.EscapeSequences;
import ui.GameplayUI;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {

  void notify(String message);
}

