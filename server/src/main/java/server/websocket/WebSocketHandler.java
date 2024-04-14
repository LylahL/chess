package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connectionManager = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session,String message){
    UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

    switch (cmd.getCommandType()){
      case JOIN_OBSERVER -> joinObserver(message, session);
      case JOIN_PLAYER -> joinPlayer(message, session);
      case MAKE_MOVE -> makeMove(message, session);
      case LEAVE -> leave(message, session);
      case RESIGN -> resign(message, session);
    }
  }

  private void resign(String message, Session session) {
    
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
