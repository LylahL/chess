package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Resign;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connectionManager = new ConnectionManager();
  private final AuthDAOInterface auth = new AuthSQL();
  private final GameDAOInterface game = new GameSQL();
  private final UserDAOInterface user = new UserSQL();
  private final GameService gameService = new GameService(auth, game, user);

  @OnWebSocketMessage
  public void onMessage(Session session,String message) throws IOException, ResponseException, DataAccessException, InvalidMoveException {
    UserGameCommand cmd = new Gson().fromJson(message, UserGameCommand.class);

    switch (cmd.getCommandType()){
      case JOIN_OBSERVER -> joinObserver(message, session);
      case JOIN_PLAYER -> joinPlayer(message, session);
      case MAKE_MOVE -> makeMove(message, session);
      case LEAVE -> leave(message, session);
      case RESIGN -> resign(message, session);
    }
  }

  private boolean authTokenCheck(String authString) throws IOException {
    if (auth.getAuthDataByAuthString(authString)==null){
      connectionManager.sendMessage(authString, new Error(ServerMessage.ServerMessageType.ERROR, "Invalid authToken"));
      return false;
    }
    return true;
  }

  private boolean gameIDCheck(int gameID, String authString) throws IOException {
    if (game.getGameByGameId(gameID)==null){
      connectionManager.sendMessage(authString, new Error(ServerMessage.ServerMessageType.ERROR, "Invalid gameID"));
      return false;
    }
    return true;
  }

  private boolean gameStillGoingCheck(GameData gameData, String authString) throws IOException {
    if (gameData.getGame().getTeamTurn()==null){
      connectionManager.sendMessage(authString, new Error(ServerMessage.ServerMessageType.ERROR, "Game is already over"));
      return false;
    }
    return true;
  }

  private void resign(String message, Session session) throws IOException, ResponseException, DataAccessException, InvalidMoveException {
    Resign resignCmd = convertCmd(message, Resign.class);
//    connectionManager.add(resignCmd.getGameID(), resignCmd.getAuthString(), session);
    int gameID = resignCmd.getGameID();
    String authString =resignCmd.getAuthString();
    if(!authTokenCheck(authString)){
      return;
    }
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    if(!gameIDCheck(gameID, authString)){
      return;
    }
    GameData gameData = game.getGameByGameId(gameID);
    String errorMsg2 = String.format("You are not in game, or you are in as an observer%d", gameID);
    if(!Objects.equals(username, gameData.getWhiteUsername()) &&
            (!Objects.equals(username, gameData.getBlackUsername()))){
      // if player not in that game
      connectionManager.sendMessage(authString, new Error(ServerMessage.ServerMessageType.ERROR, errorMsg2));
      return;
    }
    if(!gameStillGoingCheck(gameData, authString)){
      return;
    }
    ChessGame chessGame = gameData.getGame();
    chessGame.setTeamTurn(null);
    game.makeMove(gameID, chessGame);
    String resignMsg = String.format("Player %s has resigned, sucks to be them \n", username);
    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, resignMsg);
    connectionManager.broadcast(authString, notification, gameID);
  }

  private void leave(String message, Session session) {
// remove connection for leave

  }

  private void makeMove(String message, Session session) {
  }

  private void joinPlayer(String message, Session session) throws IOException {

    JoinPlayer joinCmd = convertCmd(message, JoinPlayer.class);
    String authString = joinCmd.getAuthString();
    connectionManager.add(joinCmd.getGameID(), joinCmd.getAuthString(), session);
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    int gameID = joinCmd.getGameID();
    // ignore all the error for now
    ChessGame.TeamColor teamColor = joinCmd.getPlayerColor();
    GameData gameData = game.getGameByGameId(gameID);
    ChessGame chessGame = gameData.getGame();
    String joinSuccessMsg =  String.format("Player %s has joined game as team %s", username, teamColor.toString());
    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, joinSuccessMsg);
    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.sendMessage(authString, loadGame);

  }

  private void joinObserver(String message, Session session) throws IOException {
    JoinObserver joinObserverCmd = convertCmd(message, JoinObserver.class);
    connectionManager.add(joinObserverCmd.getGameID(), joinObserverCmd.getAuthString(), session);
    String authString =joinObserverCmd.getAuthString();
    int gameID = joinObserverCmd.getGameID();
    GameData gameData = game.getGameByGameId(gameID);
    ChessGame chessGame = gameData.getGame();
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    String joinSuccessMsg = String.format("Player %s has joined as an observer", username);
    Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, joinSuccessMsg);
    LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.sendMessage(authString, loadGame);
  }

  private <T> T convertCmd(String message, Class<T> convertType){
    T response = null;
    response = new Gson().fromJson(message, convertType);
    return response;
  }
}
