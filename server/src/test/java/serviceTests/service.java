package serviceTests;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearApplication;
import service.GameService;
import service.UserService;
public class service {
  private ClearApplication clearApplication;

  private GameService gameService;
  private UserService userService;
  private AuthDAO auth;
  private GameDAO game;
  private UserDAO user;

  @BeforeEach
  void setUp() {
    auth = new AuthDAO();
    game = new GameDAO();
    user = new UserDAO();
    gameService = new GameService(auth, game, user);
    userService = new UserService(auth, user);
    clearApplication = new ClearApplication(auth, game, user);
    clearApplication.clearDataBase();
  }

  @Test
  void clearDataBase() {
  }

  @Test
  void register() {
  }

  @Test
  void login() {
  }

  @Test
  void logout() {
  }

  @Test
  void listGames() {
  }

  @Test
  void createGame() {
  }

  @Test
  void joinGame() {
  }
}
