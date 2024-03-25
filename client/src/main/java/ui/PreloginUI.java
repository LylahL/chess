package ui;

import exception.ResponseException;
import model.SignInRequest;
import model.SignInResponse;
import model.UserData;
import server.ServerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class PreloginUI {
  private State state = State.SIGNEDOUT;
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");

  private boolean isrunning = true;

  private String auth;


//  public String eval(String input) throws IOException, URISyntaxException, ResponseException {
//    var tokens = input.toLowerCase().split(" ");
//    var cmd = (tokens.length > 0) ? tokens[0] : "help";
//    // params is a copy of tokens from the second to last
//    var params =Arrays.copyOfRange(tokens, 1, tokens.length);
//    return switch (cmd) {
//      case "quit" -> quit(params);
//      case "login" -> signIn(params);
//      case "register" -> register(params);
//      default -> help();
//    };
//  }

  public void run() throws IOException, ResponseException, URISyntaxException {
    while(this.isrunning) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      ArrayList<String> cmds = new ArrayList<>(List.of(reader.readLine().split(" ")));
      parseCommads(cmds);
    }
  }

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    switch (cmd) {
      case "signin" -> signIn(params);
      case "register" -> register(params);
      case "quit" -> quit();
      default -> help();
    }
  }

  private String help() {
    return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - quit
                - help - with possible commands
                """;
  }

  private void register(String[] params) throws IOException, URISyntaxException, ResponseException {
    if (params.length == 3) {
      state = State.SIGNEDIN;
      String username = params [0];
      String password = params[1];
      String email = params[2];
      SignInResponse res = serverFacade.register(new UserData(username, password, email));
      this.auth = res.authToken();
      System.out.printf("You registered in as %s", username);
    }
  }

  private void signIn(String[] params) throws IOException, URISyntaxException, ResponseException {
    if (params.length == 2) {
      state = State.SIGNEDIN;
      String username = params [0];
      String password = params[1];
      SignInResponse res = serverFacade.signIn(new SignInRequest(username, password));
      this.auth = res.authToken();
      System.out.printf("You signed in as %s", username);
    }
  }

  private void quit() {
    isrunning = false;
    }

}

