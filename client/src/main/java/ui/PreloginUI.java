package ui;

import exception.ResponseException;
import model.SignInRequest;
import model.SignInResponse;
import server.Server;
import server.ServerFacade;
import java.util.Arrays;

public class PreloginUI {
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public PreloginUI(String serverUrl) {
    this.serverUrl=serverUrl;
  }

  public String eval(String input){
    var tokens = input.toLowerCase().split(" ");
    var cmd = (tokens.length > 0) ? tokens[0] : "help";
    // params is a copy of tokens from the second to last
    var params =Arrays.copyOfRange(tokens, 1, tokens.length);
    return switch (cmd) {
      case "quit" -> quit(params);
      case "login" -> signIn(params);
      case "register" -> register(params);
      default -> help();
    };
  }

  private String help() {
    return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess
                - help - with possible commands
                """;
  }

  private String register(String[] params) {
  }

  private String signIn(String[] params) {
    if (params.length == 3) {
      state = State.SIGNEDIN;
      String username = params [0];
      String password = params[1];
      String email = params[2];
      SignInResponse res = ServerFacade.signIn(new SignInRequest(username, password, email));
    }
  }

  private String quit(String[] params) {
  }
}
