package dataAccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearApplication;
import service.GameService;
import service.UserService;

class AuthSQLTest {
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

  }

  @Test
  void getAuthDataByAuthString() {
  }

  @Test
  void createAuthToken() {
  }

  @Test
  void deleteAuthToken() {
  }

  @Test
  void checkExist() {
  }
}