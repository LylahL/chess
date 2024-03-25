package server;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;


public class ServerFacade<T> {
  private final String serverURL;

  public ServerFacade(String url) {
    this.serverURL = url;
  }

  public SignInResponse signIn(SignInRequest signInRequest) throws IOException, URISyntaxException {
    URL path = (new URI(serverURL + "/session")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(signInRequest);
    // send Request
    HttpURLConnection http = sendRequest(path, "POST", requestBody);
    // read Response
    return readResponse(http, SignInResponse.class);
  }

  public SignInResponse register(UserData user) throws IOException, URISyntaxException {
    URL path = (new URI(serverURL + "/user")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(user);
    // send Request
    HttpURLConnection http = sendRequest(path, "POST", requestBody);
    // read Response
    return readResponse(http, SignInResponse.class);
  }

  public void logout(AuthData auth) throws URISyntaxException, IOException {
    URL path = (new URI(serverURL + "/session")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(auth.getAuthToken());
    // send Request
    HttpURLConnection http = sendRequest(path, "DELET", requestBody);
  }

  public CreateGameResponse createGame(AuthData auth, CreateGameRequest createGameRequest) {

  }
  private String WriteRequestBody(Object requestObject) {
    if(requestObject != null){
      String requestBody = new Gson().toJson(requestObject);
      return requestBody;
    }
    return null;
  }

  private HttpURLConnection sendRequest(URL url, String method, String body) throws IOException, URISyntaxException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method);
    http.setDoOutput(true);
    try (var outputStream = http.getOutputStream()) {
      outputStream.write(body.getBytes());
    }
    http.connect();
    return http;
  }

  private <T> T readResponse(HttpURLConnection http, Class<T> responseClass)  throws IOException {
    var statusCode = http.getResponseCode();
    var statusMessage = http.getResponseMessage();
    T responseBody = null ;
    try (InputStream respBody = http.getInputStream()) {
      InputStreamReader inputStreamReader =  new InputStreamReader(respBody);
      responseBody = new Gson().fromJson(inputStreamReader, responseClass);
    }
    return responseBody;

  }


}
