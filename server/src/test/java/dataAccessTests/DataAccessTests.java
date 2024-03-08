package dataAccessTests;

import dataAccess.AuthSQL;
import dataAccess.DataAccessException;
import dataAccess.GameSQL;
import dataAccess.UserSQL;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearApplication;
import service.GameService;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTests {
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

  // auth
  @Test
  void clearAuth() {
    try {
      AuthData authData = auth.createAuthToken("username");
      auth.clear();
      auth.checkExist(authData);
      assertFalse(auth.checkExist(authData));
    } catch (ResponseException | DataAccessException e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  void getAuthDataByAuthStringPositive() {
    try {
      AuthData authData = auth.createAuthToken("username");
      assertEquals(auth.getAuthDataByAuthString(authData.getAuthToken()), authData);
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getAuthDataByAuthStringNegative() {
    try {
      clearApplication.clearDataBase();
      assertNull(auth.getAuthDataByAuthString("username"));
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void createAuthTokenPositive() {
    try {
      AuthData authData = auth.createAuthToken("username");
      assertEquals(auth.getAuthDataByAuthString(authData.getAuthToken()), authData);
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void createAuthTokenNegative() {
    assertThrows(ResponseException.class, () -> auth.createAuthToken(user000.getUsername()));
  }


  @Test
  void deleteAuthTokenPositive() {
    try {
      AuthData authData = auth.createAuthToken("username");
      auth.deleteAuthToken(authData);
      assertFalse(auth.checkExist(authData));
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void deleteAuthTokenNegative() {
    assertThrows(NullPointerException.class, ()-> auth.deleteAuthToken(null));
  }

  @Test
  void checkAuthExist() {
    try {
     AuthData authData=  auth.createAuthToken("username");
     assertTrue(auth.checkExist(authData));
    } catch (ResponseException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // game
  @Test
  void getGameByGameIdPositive() throws ResponseException, DataAccessException {
    GameData gameData = game.createNewGame("game");
    GameData gameObject = game.getGameByGameId(gameData.getGameID());
    assertEquals(gameObject, gameData);
  }

  @Test
  void getGameByGameIdNegative() throws ResponseException, DataAccessException {
    GameData gameData1 = game.getGameByGameId(21);
    assertNull(gameData1);

  }
  @Test
  void createNewGamePositive() throws ResponseException, DataAccessException {
    GameData gameData = game.createNewGame("gameName");
    assertTrue(game.checkExist(gameData.getGameID()));
  }

  @Test
  void createNewGameNegative() throws ResponseException, DataAccessException {
    assertThrows(ResponseException.class, ()-> game.createNewGame(null));
  }

  @Test
  void addUserToGamePositive() throws ResponseException, DataAccessException {
  GameData gameData =  game.createNewGame("game" );
  game.addUserToGame("WHITE", gameData.getGameID(), "username");
  GameData gameData1 = game.getGameByGameId(gameData.getGameID());
  assertEquals(gameData1.getWhiteUsername(), "username");
  }
  @Test
  void addUserToGameNegative() throws ResponseException, DataAccessException {
    assertThrows(RuntimeException.class, ()-> game.addUserToGame("WHITE", 22, "username"));
  }
  @Test
  void clearAllGame() throws ResponseException, DataAccessException {
    GameData gameData = game.createNewGame("gameName");
    game.clearAllGame();
    assertFalse(game.checkExist(gameData.getGameID()));

  }


}