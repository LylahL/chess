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
import java.util.Objects;


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
    if (isrunning){
//      System.out.printf("[%s] >>>", state.toString());
      while(this.isrunning) {
        System.out.printf("[%s] >>>", state.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> cmds = new ArrayList<>(List.of(reader.readLine().split(" ")));
        parseCommads(cmds);

      }
    }
  }


  private void parseCommads(ArrayList<String> cmds) throws ResponseException, IOException, URISyntaxException {
    String cmd = cmds.getFirst().toLowerCase();
    String[] params =cmds.subList(1, cmds.size()).toArray(new String[0]);
    VaidInputCheck(cmd, params);
    switch (cmd) {
      case "signin" -> signIn(params);
      case "register" -> register(params);
      case "quit" -> quit();
      case "help" -> help();
    }
  }

  private void VaidInputCheck(String cmd, String[] params) {
    ArrayList<String> validInputList = new ArrayList<>(Arrays.asList("signin","register", "quit", "help"));
    // not a valid input
    if(!validInputList.contains(cmd)){
      System.out.println("Please input a valid command");
      help();
    }
    else if((Objects.equals(cmd, "register") && params.length != 3)||
            (Objects.equals(cmd, "signin") && params.length != 2)){
      System.out.println("Please input correct amount of params");
      help();
    }

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
      if(res != null) {
        this.auth=res.authToken();
        this.username=res.username();
        this.state=State.SIGNEDIN;
        PostloginUI postloginUI=new PostloginUI(auth, this.username, this.state);
        System.out.printf("You registered in as %s", username);
        System.out.println();
        postloginUI.run();
      }
    }
  }

  private void signIn(String[] params) throws IOException, URISyntaxException, ResponseException {
    if (params.length == 2) {
      String username = params [0];
      String password = params[1];
      SignInResponse res = serverFacade.signIn(new SignInRequest(username, password));
      if(res != null){
        this.auth = res.authToken();
        this.username = res.username();
        this.state = State.SIGNEDIN;
        System.out.printf("You signed in as %s", username);
        System.out.println();
        PostloginUI postloginUI = new PostloginUI(auth, res.username(), state);
        postloginUI.run();
      }
    }
  }

  private void quit() {
    this.state = State.SIGNEDOUT;
    isrunning = false;
    }

}

