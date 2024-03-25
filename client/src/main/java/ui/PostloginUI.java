package ui;

import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PostloginUI {
  private AuthData auth;
  private String username;
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");

  private boolean isrunning = true;

  PostloginUI (String auth, String username){
    AuthData authData = new AuthData(auth, username);
    this.auth =  authData;
    this.username = username;
  }

  public void run() throws ResponseException, IOException, URISyntaxException {
    while(isrunning){
      System.out.println(auth);
      System.out.println(username);
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      ArrayList<String> cmds = new ArrayList<>(List.of(reader.readLine().split(" ")));
      parseCommads(cmds);
    }
  }

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    switch (cmd) {
      case "logout" -> logOut(params);
      case "creategame" -> createGame(params);
      case "listgames" -> listGames(params);
      case "joingame" -> joinGame(params);
      case "joinObserver" -> joinObserver(params);
      default -> PreloginUI.help();
    }
  }

  private void joinObserver(String[] params) throws URISyntaxException, IOException {
    if (params.length == 1){
      int gameId = Integer.parseInt(params[0]);
      serverFacade.joinGame(auth, new JoinGameRequest(null, gameId));
      System.out.println("Joined as Observer Successfully");

    }

  }

  private void joinGame(String[] params) throws URISyntaxException, IOException {
    if (params.length == 2){
      String teamColor = params[0];
      int gameId = Integer.parseInt(params[1]);
      serverFacade.joinGame(auth, new JoinGameRequest(teamColor, gameId));
      System.out.println("Joined Successfully");

    }
  }

  private void listGames(String[] params) throws URISyntaxException, IOException {
    if (params.length ==0){
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
      System.out.println(response.gameID());
    }
  }

  private void logOut(String[] params) throws URISyntaxException, IOException {
    if (params.length == 0){
      serverFacade.logout(auth);
      System.out.println("Log out successfully");
    }
  }
}
