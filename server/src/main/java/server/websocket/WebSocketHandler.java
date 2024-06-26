package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
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
import webSocketMessages.serverMessages.ErrorMsg;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

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
      connectionManager.sendMessage(authString, new ErrorMsg("Invalid authToken"));
      return false;
    }
    return true;
  }

  private boolean gameIDCheck(int gameID, String authString) throws IOException {
    if (game.getGameByGameId(gameID)==null){
      connectionManager.sendMessage(authString, new ErrorMsg("Invalid gameID"));
      return false;
    }
    return true;
  }

  private boolean gameStillGoingCheck(GameData gameData, String authString) throws IOException {
    if (gameData.getGame().getGameOver() == 1){
      connectionManager.sendMessage(authString, new ErrorMsg("Game is already over"));
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
    String errorMsg2 = String.format("Error: You are not in game%d, or you are in as an observer", gameID);
    if(!Objects.equals(username, gameData.getWhiteUsername()) &&
            (!Objects.equals(username, gameData.getBlackUsername()))){
      // if player not in that game
      connectionManager.sendMessage(authString, new ErrorMsg(errorMsg2));
      return;
    }
    if(!gameStillGoingCheck(gameData, authString)){
      return;
    }
    ChessGame chessGame = gameData.getGame();
    chessGame.setGameOver(1);
    game.makeMove(gameID, chessGame);
    String resignMsg = String.format("Player %s has resigned, sucks to be them \n", username);
    Notification notification = new Notification(resignMsg);
    connectionManager.broadcast(authString, notification, gameID);
  }

  private void leave(String message, Session session) throws IOException {
// remove connection for leave
    Leave leaveCmd = convertCmd(message, Leave.class);
    int gameID = leaveCmd.getGameID();
    String authString = leaveCmd.getAuthString();
    String username = auth.getAuthDataByAuthString(authString).getUsername();
    String successMessage = String.format("Player %s has left the game, now they are a observer", username);
    ChessGame chessGame = game.getGameByGameId(gameID).getGame();
    Notification notification = new Notification(successMessage);
    connectionManager.sendMessage(authString, new LoadGame(chessGame));
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.removeSession(gameID, authString);

  }

  private void makeMove(String message, Session session) throws ResponseException, DataAccessException, IOException, InvalidMoveException {
    MakeMove makeMoveCmd = convertCmd(message, MakeMove.class);
    String authString = makeMoveCmd.getAuthString();
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    int gameID = makeMoveCmd.getGameID();
    GameData gameData = game.getGameByGameId(gameID);
    ChessGame chessGame = gameData.getGame();
    //if game is already over
    if(chessGame.getGameOver() == 1){
      connectionManager.sendMessage(authString, new ErrorMsg("You can't make move, game is over"));
    }
    //if it's an observer
    if(!Objects.equals(username, gameData.getWhiteUsername()) &&
            (!Objects.equals(username, gameData.getBlackUsername()))){
      // if player not in that game
      connectionManager.sendMessage(authString, new ErrorMsg("You are an Observer that shouldn't be making a move"));
      return;
    }
    ChessGame updatedChessgame = game.getGameByGameId(gameID).getGame();
    // check turn
    if(Objects.equals(username, gameData.getWhiteUsername())){
      // white is playing
      if(updatedChessgame.getTeamTurn() != ChessGame.TeamColor.WHITE){
        connectionManager.sendMessage(authString, new ErrorMsg("Not your turn"));
        return;
      }
    }
    else if (Objects.equals(username, gameData.getBlackUsername()) && (updatedChessgame.getTeamTurn() != ChessGame.TeamColor.BLACK)){
        connectionManager.sendMessage(authString, new ErrorMsg("Not your turn"));
        return;

    }
    ChessMove chessMove = makeMoveCmd.getMove();
    try {
      gameService.makeMove(gameID, chessMove, authData);
    } catch (Exception e){
      connectionManager.sendMessage(authString, new ErrorMsg(e.getMessage()));
      return;
    }
    ChessGame gameAfterMove = game.getGameByGameId(gameID).getGame();
    String successMsg = String.format("Player %s has made a move from %s to %s", username, chessMove.getStartPosition(), chessMove.getEndPosition());
    Notification notification = new Notification(successMsg);
    LoadGame loadGame = new LoadGame(gameAfterMove);
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.broadcast(authString, loadGame, gameID);
    // checkmate stalemate checks
    if (gameAfterMove.isInCheck(ChessGame.TeamColor.BLACK)){
      connectionManager.broadcast(authString,new Notification("Team Black is in check"), gameID);
    }
    if (gameAfterMove.isInCheck(ChessGame.TeamColor.WHITE)){
      connectionManager.broadcast(authString,new Notification("Team White is in check"), gameID);
    }
    if (gameAfterMove.isInCheckmate(ChessGame.TeamColor.BLACK)){
      gameAfterMove.setTeamTurn(null);
      gameAfterMove.setGameOver(1);
      game.makeMove(gameID, gameAfterMove);
      connectionManager.broadcast(authString, new Notification("Team White has won"), gameID);
    }
    if (gameAfterMove.isInCheckmate(ChessGame.TeamColor.WHITE)){
      gameAfterMove.setTeamTurn(null);
      gameAfterMove.setGameOver(1);
      game.makeMove(gameID, gameAfterMove);
      connectionManager.broadcast(authString, new Notification("Team Black has won"), gameID);
    }

  }

  private void joinPlayer(String message, Session session) throws IOException {

    JoinPlayer joinCmd = convertCmd(message, JoinPlayer.class);
    String authString = joinCmd.getAuthString();
    connectionManager.add(joinCmd.getGameID(), joinCmd.getAuthString(), session);
    int gameID = joinCmd.getGameID();
    if (auth.getAuthDataByAuthString(authString)==null) {
      connectionManager.broadcast(authString, new ErrorMsg("Invalid authToken"), gameID);
      return;
    }
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    if(!gameIDCheck(gameID, authString)){
      return;
    }
    // ignore all the error for now
    ChessGame.TeamColor teamColor = joinCmd.getPlayerColor();
    GameData gameData = game.getGameByGameId(gameID);
    ChessGame chessGame = gameData.getGame();
    // this is the without http call one
    if(gameData.getBlackUsername()==null||gameData.getWhiteUsername()==null){
      connectionManager.sendMessage(authString, new ErrorMsg("Haven't made HTTP call yet"));
      return;
    }
    if (teamColor == ChessGame.TeamColor.WHITE && !Objects.equals(gameData.getWhiteUsername(), username)){
      connectionManager.sendMessage(authString, new ErrorMsg("This team White is already taken"));
      return;
    }
    if (teamColor == ChessGame.TeamColor.BLACK && !Objects.equals(gameData.getBlackUsername(), username)){
      connectionManager.sendMessage(authString, new ErrorMsg("This team Black is already taken"));
      return;
    }
    String joinSuccessMsg =  String.format("Player %s has joined game as team %s", username, teamColor.toString());
    Notification notification = new Notification(joinSuccessMsg);
    LoadGame loadGame = new LoadGame(chessGame);
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.sendMessage(authString, loadGame);

  }

  private void joinObserver(String message, Session session) throws IOException {
    JoinObserver joinObserverCmd = convertCmd(message, JoinObserver.class);
    connectionManager.add(joinObserverCmd.getGameID(), joinObserverCmd.getAuthString(), session);
    String authString =joinObserverCmd.getAuthString();
    int gameID = joinObserverCmd.getGameID();
    if(!authTokenCheck(authString)){
      return;
    }
    if(!gameIDCheck(gameID, authString)){
      return;
    }

    GameData gameData = game.getGameByGameId(gameID);
    ChessGame chessGame = gameData.getGame();
    AuthData authData = auth.getAuthDataByAuthString(authString);
    String username = authData.getUsername();
    String joinSuccessMsg = String.format("Player %s has joined as an observer", username);
    Notification notification = new Notification(joinSuccessMsg);
    LoadGame loadGame = new LoadGame(chessGame);
    connectionManager.sendMessageToOthers(authString, notification, gameID);
    connectionManager.sendMessage(authString, loadGame);
  }

  private <T> T convertCmd(String message, Class<T> convertType){
    T response = null;
    response = new Gson().fromJson(message, convertType);
    return response;
  }
}
