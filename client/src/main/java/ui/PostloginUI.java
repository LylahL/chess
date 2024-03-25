package ui;

import exception.ResponseException;
import model.AuthData;
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
      case "joingame" -> joingame(params);
      case "joinObserver" -> joinObserver(params);
      default -> PreloginUI.help();
    }
  }

  private void joinObserver(String[] params) {
  }

  private void joingame(String[] params) {
  }

  private void listGames(String[] params) {
  }

  private void createGame(String[] params) {

  }

  private void logOut(String[] params) throws URISyntaxException, IOException {
    if (params.length == 0){
      serverFacade.logout(auth);
      System.out.println("Log out successfully");
    }
  }
}
