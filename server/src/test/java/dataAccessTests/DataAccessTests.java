package dataAccessTests;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearApplication;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataAccessTests {
  private AuthSQL auth;
  private GameSQL game;
  private UserSQL user;
  private GameService gameService;
  private UserService userService;
  private ClearApplication clearApplication;

  UserData user123 = new UserData("username", "password", "email");
  UserData user000 = new UserData(null, null, null);

  @BeforeEach
  void setUp() throws ResponseException, DataAccessException {

    auth = new AuthSQL();
    game = new GameSQL();
    user = new UserSQL();
    gameService = new GameService(auth, game, user);
    userService = new UserService(auth, user);
    clearApplication = new ClearApplication(auth, game, user);
    clearApplication.clearDataBase();
  }


  // Positive
  @Test
  void clearDataBase() throws ResponseException, DataAccessException {
    user.createUser(user123);
    game.createNewGame("chessGame");
    auth.createAuthToken("username");
    clearApplication.clearDataBase();
    assertThrows(ResponseException.class, ()-> userService.login(user123));
  }
  @Test
  void register() throws ResponseException, DataAccessException {
    userService.register(user123);
    assertEquals(user123, user.getUserByUsername(user123.getUsername()));
  }

  @Test
  void registerFail() {
    try {
      // null user
      userService.register(user000);
    }catch (ResponseException e){
      assertEquals(400, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void login() throws ResponseException, DataAccessException {
    user.createUser(user123);
    AuthData authToken = userService.login(user123);
    assertEquals(authToken, auth.getAuthDataByAuthString(authToken.getAuthToken()));
  }

  @Test
  void loginFail() {
    try {
      user.createUser(user123);
      UserData userFalse = new UserData("username", "wrongPassword","email");
      userService.login(userFalse);
    } catch (ResponseException e){
      assertEquals(401, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void logout() throws ResponseException, DataAccessException {
    user.createUser(user123);
    AuthData authToken = userService.login(user123);
    userService.logout(authToken);
    assertNull(auth.getAuthDataByAuthString(authToken.getAuthToken()));
  }


  @Test
  void logoutFail()  {
    try{
      AuthData authToken = auth.createAuthToken("user");
      userService.logout(authToken);
    }catch (ResponseException e){
      assertEquals(401, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void listGames() throws ResponseException, DataAccessException {
    game.createNewGame("game1");
    game.createNewGame("game2");
    AuthData authToken = userService.register(user123);
    assertEquals(gameService.listGames(authToken), game.listAllGame());
  }
  @Test
  void listGamesFailed(){
    try {
      game.createNewGame("game1");
      game.createNewGame("game2");
      AuthData authToken = auth.createAuthToken("username");
      gameService.listGames(authToken);
    }catch (ResponseException e){
      assertEquals(401, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }

  }
  @Test
  void createGame() throws ResponseException, DataAccessException {
    AuthData authToken = userService.register(user123);
    int gameID = gameService.createGame(authToken, "gameName");
    assertTrue(game.checkExist(gameID));
  }

  @Test
  void createGameFailed() {
    try{
      AuthData authToken = new AuthData("username");
      int gameID = gameService.createGame(authToken, "gameName");
    }catch (ResponseException e){
      assertEquals(401, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void joinGame() throws ResponseException, DataAccessException {
    AuthData authToken = userService.register(user123);
    int gameId = gameService.createGame(authToken, "gameName");
    gameService.joinGame(gameId, "WHITE", authToken);
    assertEquals(gameId, game.getGameByGameId(gameId).getGameID());
  }

  @Test
  void joinGameFailed(){
    try {
      AuthData authToken = userService.register(user123);
      gameService.joinGame(14, "WHITE", authToken);
    }catch(ResponseException e){
      assertEquals(400, e.statusCode());
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
