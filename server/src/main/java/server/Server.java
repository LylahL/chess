package server;

import com.google.gson.Gson;
import dataAccess.*;
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
    private AuthDAO authDAO;

    public Server() {
        // what should I do with the constructor
        try {
            DatabaseManager.configureDatabase();
        } catch (ResponseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
           e.printStackTrace();
           throw new RuntimeException(e);
        }
        this.authDAO = new AuthDAO();
        GameDAO game = new GameDAO();
        UserDAO user = new UserDAO();
        userService = new UserService(authDAO, user);
        clearApplication = new ClearApplication(authDAO, game, user);
        gameService = new GameService(authDAO, game, user);
    }

    public record JoinGameRequest(String playerColor, int gameID) {
    }
    public record LoginResult(String username, String authToken) {
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
            JoinGameRequest joinGameRequest = new Gson().fromJson(request.body(), JoinGameRequest.class);
            String authToken = request.headers("authorization");
            AuthData auth = authDAO.getAuthDataByAuthString(authToken);
            int gameID = joinGameRequest.gameID();
            String playerColor = joinGameRequest.playerColor();
            gameService.joinGame(gameID, playerColor, auth);
            response.status(200 );
            return "{}";
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object listGames(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            AuthData auth = authDAO.getAuthDataByAuthString(authToken);
            HashSet<GameData> games = gameService.listGames(auth);
            response.status(200);
            // return "{ \"gameID\": \"" + gameID + "\" }";
            return "{ \"games\": " + new Gson().toJson(games) + "}";
        } catch (ResponseException e) {
          exceptionHandler(e, request, response);
          return getErrorMassage(e);
        }
    }

    private Object createGame(Request request, Response response) {
        try {
            String authToken = request.headers("authorization");
            AuthData auth = authDAO.getAuthDataByAuthString(authToken);
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
            String authToken = request.headers("authorization");
            AuthData auth = authDAO.getAuthDataByAuthString(authToken);
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
            LoginResult result = new LoginResult(user.getUsername(), auth.getAuthToken());
            return new Gson().toJson(result);
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
