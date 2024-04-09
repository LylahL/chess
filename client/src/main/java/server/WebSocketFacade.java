package server;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint{

  Session session;
  NotificationHandler notificationHandler;
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
  // make connection to the /connect endpoint in my server
  public WebSocketFacade(String url, NotificationHandler notificationHandler)throws ResponseException {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);
      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
          notificationHandler.notify(message);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }

  public void joinPlayer(AuthData auth, int gameID, ChessGame.TeamColor playerColor){
    try{
      var cmd = new JoinPlayer(auth.getAuthToken(), gameID, playerColor);
      //sends a text message over the WebSocket connection, and the message content is the cmd object.
      this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void joinObserver(AuthData auth, int gameID){
    try {
      var cmd = new JoinObserver(auth.getAuthToken(), gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void makeMove(AuthData auth, int gameID, ChessMove move){
    try {
      var cmd = new MakeMove(auth.getAuthToken(), gameID, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void leave(AuthData auth, int gameID){
    try {
      var cmd = new Leave(auth.getAuthToken(), gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void resign(AuthData auth, int gameID){
    try {
      var cmd = new Resign(auth.getAuthToken(), gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(cmd));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
