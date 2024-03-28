package clientTests;

import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static final UserData user = new UserData("hello", "goodmorning", "email");

    private static final SignInRequest signInRequest = new SignInRequest("hello", "goodmorning");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @BeforeEach
    public void setUp() throws URISyntaxException, IOException { serverFacade.clear(); }

    @AfterEach
    public void tearDown() throws URISyntaxException, IOException { serverFacade.clear(); }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        serverFacade.logout(new AuthData(auth, user.getUsername()));
    }

    @Test
    public void registerNegative() throws IOException, URISyntaxException {
        serverFacade.register(user);
        try{
            serverFacade.register(user);
        } catch (Exception e) {
            assertEquals(403, e.getMessage());
        }
    }

    @Test
    public void loginPositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        serverFacade.logout(new AuthData(auth, user.getUsername()));
        String auth2 = serverFacade.signIn(signInRequest).authToken();
        serverFacade.logout(new AuthData(auth2, signInRequest.username()));
    }

    @Test
    public void loginNegative() {
        try{
            serverFacade.signIn(signInRequest);
        } catch (Exception e) {
            assertEquals(401, e.getMessage());
        }
    }
    @Test
    public void logoutPositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        serverFacade.logout(new AuthData(auth, user.getUsername()));
    }

    @Test
    public void logoutNegative() {
        try{
            serverFacade.logout(new AuthData("username"));
        } catch (Exception e) {
            assertEquals(401, e.getMessage());
        }
    }

    @Test
    public void createGamePositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        AuthData authData = new AuthData(auth, user.getUsername());
        serverFacade.createGame(authData, new CreateGameRequest("gamename"));
        serverFacade.listGames(authData);
        assertEquals(1,serverFacade.listGames(authData).games().size());
    }

    @Test
    public void createGameNegative() throws IOException, URISyntaxException {
        try{
            serverFacade.createGame(new AuthData("temp"), new CreateGameRequest("gametest"));
        } catch (Exception e) {
            assertEquals(401, e.getMessage());
        }
    }
    @Test
    public void listGamesPositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        AuthData authData = new AuthData(auth, user.getUsername());
        serverFacade.createGame(authData, new CreateGameRequest("gamename"));
        serverFacade.listGames(authData);
        assertEquals(1,serverFacade.listGames(authData).games().size());
    }

    @Test
    public void listGamesNegative() throws IOException, URISyntaxException {
        try{
            serverFacade.listGames(new AuthData("temp"));
        } catch (Exception e) {
            assertEquals(401, e.getMessage());
        }
    }
    @Test
    public void joinGamePositive() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        AuthData authData = new AuthData(auth, user.getUsername());
        serverFacade.createGame(authData, new CreateGameRequest("gamename"));
        ServerFacade.joinGame(authData, new JoinGameRequest("WHITE", 0));
    }

    @Test
    public void joinGameNegative() throws IOException, URISyntaxException {
        try{
            String auth = serverFacade.register(user).authToken();
            AuthData authData = new AuthData(auth, user.getUsername());
            serverFacade.createGame(authData, new CreateGameRequest("gamename"));
        } catch (Exception e) {
            assertEquals(401, e.getMessage());
        }
    }

    @Test
    public void clear() throws IOException, URISyntaxException {
        String auth = serverFacade.register(user).authToken();
        AuthData authData = new AuthData(auth, user.getUsername());
        serverFacade.createGame(authData, new CreateGameRequest("gamename"));
        serverFacade.clear();
        assertEquals(null ,serverFacade.listGames(authData));
    }
    }
