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
import java.util.Arrays;
import java.util.List;


public class PreloginUI {
  private State state = State.SIGNEDOUT;
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");

  private boolean isrunning = true;
  private String auth;
  private String username;

  public PreloginUI(State state) {
    this.state = state;
  }

  public void run() throws IOException, ResponseException, URISyntaxException {
    System.out.printf("[%s] >>>", state.toString());
    while(this.isrunning) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      ArrayList<String> cmds = new ArrayList<>(List.of(reader.readLine().split(" ")));
      parseCommads(cmds);
      System.out.printf("[%s] >>>", state.toString());
    }
  }

  // TODO: Error Handling
  // TODO: Invalid input handling
  // TODO: Print the board right
  // TODO: Write Unit tests

  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    VaidInputCheck(cmds);
    switch (cmd) {
      case "signin" -> signIn(params);
      case "register" -> register(params);
      case "quit" -> quit();
      default -> help();
    }
  }

  private void VaidInputCheck(ArrayList<String> cmds) {
    ArrayList<String> list = new ArrayList<>(Arrays.asList("");

  }

  static void help() {
    System.out.print("""
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - signIn <USERNAME> <PASSWORD> - to play chess
                - quit - quit
                - help - with possible commands
                """);
  }

  private void register(String[] params) throws IOException, URISyntaxException, ResponseException {
    if (params.length == 3) {
      String username = params [0];
      String password = params[1];
      String email = params[2];
      SignInResponse res = serverFacade.register(new UserData(username, password, email));
      this.auth = res.authToken();
      this.username = res.username();
      this.state = State.SIGNEDIN;
      PostloginUI postloginUI = new PostloginUI(auth, this.username, this.state);
      System.out.printf("You registered in as %s \n", username);
      postloginUI.run();
    }
  }

  private void signIn(String[] params) throws IOException, URISyntaxException, ResponseException {
    if (params.length == 2) {
      String username = params [0];
      String password = params[1];
      SignInResponse res = serverFacade.signIn(new SignInRequest(username, password));
      this.auth = res.authToken();
      this.username = res.username();
      this.state = State.SIGNEDIN;
      System.out.printf("You signed in as %s", username);
      PostloginUI postloginUI = new PostloginUI(auth, res.username(), state);
      postloginUI.run();
    }
  }

  private void quit() {
    isrunning = false;
    }

}

