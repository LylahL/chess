package server.websocket;

import com.google.gson.Gson;
import dataAccess.AuthDAOInterface;
import dataAccess.AuthSQL;
import dataAccess.GameDAOInterface;
import dataAccess.GameSQL;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.Resign;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connectionManager = new ConnectionManager();
  private final AuthDAOInterface auth = new AuthSQL();
  private final GameDAOInterface game = new GameSQL();

  @OnWebSocketMessage
  public void onMessage(Session session,String message) throws IOException {
    UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

    switch (cmd.getCommandType()){
      case JOIN_OBSERVER -> joinObserver(message, session);
      case JOIN_PLAYER -> joinPlayer(message, session);
      case MAKE_MOVE -> makeMove(message, session);
      case LEAVE -> leave(message, session);
      case RESIGN -> resign(message, session);
    }
  }

  private void resign(String message, Session session) throws IOException {
    Resign resignCmd = convertCmd(message, Resign.class);
    connectionManager.add(resignCmd.getGameID(), resignCmd.getAuthString(), session);
    int gameID = resignCmd.getGameID();
    String authString =resignCmd.getAuthString();
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    GameData gameData = game.getGameByGameId(gameID);
    String resignMsg = String.format("Player %s has resigned, sucks to be them \n", username);
    String errorMsg = String.format("You are not in game %d \n", gameID);

    if(!Objects.equals(username, gameData.getWhiteUsername()) &&
            (!Objects.equals(username, gameData.getBlackUsername()))){
      // if player not in that game
      connectionManager.sendMessage(authString, new Error(ServerMessage.ServerMessageType.ERROR, errorMsg), gameID);
    }


  }

  private void leave(String message, Session session) {
  }

  private void makeMove(String message, Session session) {
  }

  private void joinPlayer(String message, Session session) {
  }

  private void joinObserver(String message, Session session) {
  }

  private <T> T convertCmd(String message, Class<T> convertType){
    T response = null;
    response = new Gson().fromJson(message, convertType);
    return response;
  }
}
