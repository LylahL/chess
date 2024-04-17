package ui;

import server.ServerFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClientRun {
  private static State state = State.SIGNEDOUT;
  private final ServerFacade serverFacade = new ServerFacade("http://localhost:8282");

  private boolean isrunning = true;
  private String auth;
  private String username;

  public static ArrayList<String> gatherData() throws IOException {
    System.out.printf("[%s] >>>", state.toString());
    BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
   ArrayList<String> cmds=new ArrayList<>(List.of(reader.readLine().split(" ")));
   return cmds;
  }



}
