package server;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


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
    HttpURLConnection http = sendRequest(path, "POST", requestBody, null, null);
    // read Response
    return readResponse(http, SignInResponse.class);
  }

  public SignInResponse register(UserData user) throws IOException, URISyntaxException {
    URL path = (new URI(serverURL + "/user")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(user);
    // send Request
    HttpURLConnection http = sendRequest(path, "POST", requestBody, null, null);
    // read Response
    return readResponse(http, SignInResponse.class);
  }

  public void logout(AuthData auth) throws URISyntaxException, IOException {
    URL path = (new URI(serverURL + "/session")).toURL();
    // write Request Body
//    String requestBody = WriteRequestBody(auth.getAuthToken());
    // send Request
    HttpURLConnection http = sendRequest(path, "DELETE", null, "authorization", auth.getAuthToken());
    readResponse(http, null);
    System.out.println("succeed");
  }

  public CreateGameResponse createGame(AuthData auth, CreateGameRequest createGameRequest) throws URISyntaxException, IOException {
    URL path = (new URI(serverURL + "/game")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(createGameRequest);
    // send Request
    HttpURLConnection http = sendRequest(path, "POST", requestBody, "authorization", auth.getAuthToken());
    // read Response
    return readResponse(http, CreateGameResponse.class);
  }

  public ListGameResponse listGames(AuthData auth) throws URISyntaxException, IOException {
    URL path = (new URI(serverURL + "/game")).toURL();
    // write Request Body
//    String requestBody = WriteRequestBody(auth.getAuthToken());
    // send Request
    HttpURLConnection http = sendRequest(path, "GET", null, "authorization", auth.getAuthToken());
    // read Response
    return readResponse(http,ListGameResponse.class);
  }

  public void joinGame(AuthData auth, JoinGameRequest joinGameRequest) throws URISyntaxException, IOException {
    URL path = (new URI(serverURL + "/game")).toURL();
    // write Request Body
    String requestBody = WriteRequestBody(joinGameRequest);
    // send Request
    HttpURLConnection http = sendRequest(path, "PUT", requestBody, "authorization", auth.getAuthToken());
    readResponse(http, null);
    // erorr handling
//    { if code 403 then systme out "already taken"
    // catch all execption geterrormessageprint
    System.out.println("succeed");
  }

  private String WriteRequestBody(Object requestObject) {
    if(requestObject != null){
      String requestBody = new Gson().toJson(requestObject);
      return requestBody;
    }
    return null;
  }


  private HttpURLConnection sendRequest(URL url, String method, String body, String headerKey, String headerValue) throws IOException {
    HttpURLConnection http = (HttpURLConnection) url.openConnection();
    http.setRequestMethod(method);
    http.setDoOutput(true);
    if (headerKey != null && headerValue != null) {
      http.setRequestProperty(headerKey, headerValue);
    }
    if (body != null){
      try (var outputStream = http.getOutputStream()) {
        outputStream.write(body.getBytes());
      }
    }
    http.connect();
    return http;
  }

  private <T> T readResponse(HttpURLConnection http, Class<T> responseClass)  throws IOException {
    var statusCode = http.getResponseCode();
    var statusMessage = http.getResponseMessage();
    if(statusCode != 200){
      ErrorHandling(statusCode, statusMessage);
      return null;
    }
    T responseBody = null ;
    try (InputStream respBody = http.getInputStream()) {
      if (responseClass != null) {
        InputStreamReader inputStreamReader =  new InputStreamReader(respBody);
        responseBody = new Gson().fromJson(inputStreamReader, responseClass);
      }
      else {
        return null;
      }

    }
    return responseBody;

  }

  private Object ErrorHandling(int statusCode, String statusMessage) {
    System.out.printf("Status Code:%d , Error:%String",statusCode, statusMessage);
    System.out.println();
    return null;
  }
}
