package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearApplication;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.HashSet;

/*The Server receives network HTTP requests and sends them to the correct handler for processing.
  The server should also handle all unhandled exceptions that your application generates and return the appropriate HTTP status code.
*/

public class Server {
    private ClearApplication clearApplication;

    private GameService gameService;
    private UserService userService;

    public Server() {
        // what should I do with the constructor
        AuthDAO auth = new AuthDAO();
        GameDAO game = new GameDAO();
        UserDAO user = new UserDAO();
        userService = new UserService(auth, user);
        clearApplication = new ClearApplication(auth, game, user);
        gameService = new GameService(auth, game, user);
    }

    public record joinGameRequest(String clientColor, int gameID) {
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here
        //clear application
        Spark.delete("/db", this::deleteDatabase);
        //Register
        Spark.post("/user", this::register);
        //Login
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
    }

    private Object joinGame(Request request, Response response) {
        try{
            AuthData auth = new AuthData(request.headers("authorization"));
            joinGameRequest joinGameRequest = new Gson().fromJson(request.body(), Server.joinGameRequest.class);
            int gameID = joinGameRequest.gameID();
            String clientColor = joinGameRequest.clientColor();
            gameService.joinGame(gameID, clientColor, auth);
            response.status(200 );
            return "{}";
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object listGames(Request request, Response response) {
        try {
            AuthData auth = new AuthData(request.headers("authorization"));
            HashSet<GameData> games = gameService.listGames(auth);
            response.status(200);
            return new Gson().toJson(games);
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object createGame(Request request, Response response) {
        try {
            AuthData auth = new AuthData(request.headers("authorization"));
            GameData game = new Gson().fromJson(request.body(), GameData.class);
            String gameName = game.getGameName();
            int gameID =gameService.createGame(auth, gameName);
            response.status(200);
            return "{ \"gameID\": \"" + gameID + "\" }";
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object logout(Request request, Response response) {
        try {
            AuthData auth= new AuthData(request.headers("authorization"));
            userService.logout(auth);
            response.status(200);
            return "{}";
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object login(Request request, Response response) {
        try {
            UserData user = new Gson().fromJson(request.body(), UserData.class);
            AuthData auth = userService.login(user);
            response.status(200);
            return new Gson().toJson(auth);
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    public void stop() {
        Spark.stop();
    }

    private String getErrorMassage(ResponseException e){
        return "{ \"message\": \"" + e.getMessage() + "\" }";
    }
    public Object register(Request req, Response res) {
        try {
            UserData user = new Gson().fromJson(req.body(), UserData.class);
            AuthData auth = userService.register(user);
            return new Gson().toJson(auth);
        } catch (ResponseException e) {
          exceptionHandler(e, req, res);
          return getErrorMassage(e);
        }
    }
    private Object deleteDatabase(Request request, Response response) {
        clearApplication.clearDataBase();
        response.status(200);
        return "{}";
    }
}
