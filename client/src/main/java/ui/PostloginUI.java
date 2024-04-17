package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import server.WebSocketFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PostloginUI {
  private AuthData auth;
  private String username;

  private State state;
  private WebSocketFacade webSocketFacade = new WebSocketFacade("http://localhost:8282");
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");

  private boolean isrunning = true;


  PostloginUI (String auth, String username, State state) throws ResponseException {
    this.state=state;
    AuthData authData = new AuthData(auth, username);
    this.auth =  authData;
    this.username = username;
  }

  public void run() throws ResponseException, IOException, URISyntaxException {
    if(isrunning){
      System.out.println("This is the Postlogin page, type help to check available commands");
//      System.out.printf("[%s] >>>", state.toString());
      while(isrunning){
        ArrayList<String> cmds = ClientRun.gatherData();
        parseCommads(cmds);
    }
    }
  }

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    validInputCheck(cmd, params);
    switch (cmd) {
      case "logout" -> logOut(params);
      case "creategame" -> createGame(params);
      case "listgames" -> listGames(params);
      case "joingame" -> joinGame(params);
      case "joinobserver" -> joinObserver(params);
      case "help" -> help();
    }
  }

  private void validInputCheck(String cmd, String[] params) {
    ArrayList<String> validInputList=new ArrayList<>(Arrays.asList("logout", "creategame", "listgames", "joingame", "joinobserver", "help"));
    // not a valid input
    if (!validInputList.contains(cmd)) {
      System.out.println("Please input a valid command");
      help();
    } else if ((Objects.equals(cmd, "creategame") && params.length != 1)
            || (Objects.equals(cmd, "joingame") && params.length != 2) ||
            (Objects.equals(cmd, "joinobserver") && params.length != 1)) {
      System.out.println("Please input correct amount of params");
      help();
    }
  }
  static void help() {
    System.out.print("""
                - logout - logs out the user
                - createGame <GAMENAME> - create a game
                - listGames - lists all games
                - joinGame <COLOR> <GAMEID> - join a game as player
                - joinObserver <GAMEID> - join a game as observer
                """);
  }

  private void joinObserver(String[] params) throws URISyntaxException, IOException, ResponseException {
    if (params.length == 1){
      int gameId = Integer.parseInt(params[0]);
      serverFacade.joinGame(auth, new JoinGameRequest(null, gameId));
      webSocketFacade.joinObserver(auth, gameId);
      System.out.println("Joined as Observer Successfully");
      GameplayUI gameplayUI = new GameplayUI(auth.getAuthToken(), username, gameId, state);
      gameplayUI.run();
    }

  }

  private void joinGame(String[] params) throws URISyntaxException, IOException, ResponseException {
    if (params.length == 2){
      String teamColor = params[0];
      int gameId = Integer.parseInt(params[1]);
      HttpURLConnection http = ServerFacade.joinGame(auth, new JoinGameRequest(teamColor, gameId));
      ChessGame.TeamColor color= null ;
      if(Objects.equals(teamColor, "WHITE")){
       color =ChessGame.TeamColor.WHITE;
      }else if (Objects.equals(teamColor, "BLACK")){
        color = ChessGame.TeamColor.BLACK;
      }
      webSocketFacade.joinPlayer(auth, gameId, color);

      var statusCode=http.getResponseCode();
      if (statusCode == 200){
        GameplayUI gameplayUI = new GameplayUI(auth.getAuthToken(), username, gameId, state);
        gameplayUI.run();
      }
    }
  }

  private void listGames(String[] params) throws URISyntaxException, IOException {
    if (params.length == 0){
      ListGameResponse response = serverFacade.listGames(auth);
      StringBuilder result = new StringBuilder();
      for (GameData game: response.games()){
        result.append(String.format("Game ID: %s%n", game.getGameID()));
        result.append(String.format("Game Name: %s%n", game.getGameName()));
        result.append(String.format("White Username: %s%n", game.getWhiteUsername()));
        result.append(String.format("Black Username: %s%n", game.getBlackUsername()));
        result.append("\n");
      }
      System.out.println(result);
    }
  }

  private void createGame(String[] params) throws URISyntaxException, IOException {
    if (params.length == 1){
      String gameName = params[0];
      CreateGameResponse response = serverFacade.createGame(auth, new CreateGameRequest(gameName));
      System.out.printf("New game created, gameId: %d", response.gameID());
      System.out.println();
    }
  }

  private void logOut(String[] params) throws URISyntaxException, IOException, ResponseException {
    if (params.length == 0){
      serverFacade.logout(auth);
      System.out.println("Log out successfully");
    }
    this.state = State.SIGNEDOUT;
    this.isrunning = false;
//    PreloginUI preloginUI = new PreloginUI(state);
//    preloginUI.run();
  }
}
