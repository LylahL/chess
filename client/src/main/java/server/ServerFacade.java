package server;

import model.SignInRequest;

public class ServerFacade {
  private final String url;
  private final String serverPort;
  public ServerFacade(String url, String serverPort) {
    this.url = url;
    this.serverPort=serverPort;
  }

  public SignInRequest signIn(SignInRequest signInRequest){
    var path = "/user";
    return this.
  }

}
