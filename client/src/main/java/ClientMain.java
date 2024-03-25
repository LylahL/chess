import exception.ResponseException;
import ui.PreloginUI;

import java.io.IOException;
import java.net.URISyntaxException;

public class ClientMain {
  private static final PreloginUI preloginUI = new PreloginUI();

  public static void main(String[] args) throws IOException, ResponseException, URISyntaxException {
    System.out.println("♕ Welcome to chess ♕");
    preloginUI.run();
  }
}
