
import exception.ResponseException;
import ui.PreloginUI;
import ui.State;

import java.io.IOException;
import java.net.URISyntaxException;

public class ClientMain {
  private static final PreloginUI preloginUI = new PreloginUI(State.SIGNEDOUT);

  public static void main(String[] args) throws IOException, ResponseException, URISyntaxException {
    System.out.println("♕ Welcome to chess, Type Help to get started ♕");
    preloginUI.run();
  }
}
