package client;

import chess.ChessGame;
import error.ResponseError;
import error.UnauthorizedError;
import exception.ResponseException;
import model.GameData;
import model.ListGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.JoinGameRequest;
import server.Server;
import server.ServerFascade;
import service.AuthService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFascadeTests {
    static Server server;
    static ServerFascade serverFascade;
    static int game1ID;
    static int game2ID;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost:"+port;
        serverFascade = new ServerFascade(url);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        serverFascade.clear();
        serverFascade.register(new UserData("abc", "123", "hi"));
        serverFascade.register(new UserData("def", "123", "hi"));
        serverFascade.login(new UserData("abc", "123", "hi"));
        game1ID = serverFascade.createGame("game1").gameID();
        game2ID = serverFascade.createGame("game2").gameID();
    }

//    @AfterEach
//    public void resetServer() throws ResponseException {
//        serverFascade.clear();
//    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }




    @Test
    @Order(0)
    @DisplayName("Check register works")
    public void checkRegister() throws ResponseException {
        serverFascade.clear();
        assertDoesNotThrow(() -> serverFascade.register(new UserData("check1", "mypwd", "hi")));
    }

    @Test
    @Order(1)
    @DisplayName("Check register fails with duplicate username")
    public void checkRegisterFails() throws ResponseException {
        assertThrows(ResponseException.class, () -> serverFascade.register(new UserData("def", "123", "hi")));
    }

    @Test
    @Order(2)
    @DisplayName("Check login works")
    public void checkLogin() throws ResponseException {
        assertDoesNotThrow(() -> serverFascade.login(new UserData("abc", "123", "hi")));
    }

    @Test
    @Order(3)
    @DisplayName("Check login fails with invalid user")
    public void checkLoginFails() throws ResponseException {
        assertThrows(ResponseException.class, () -> serverFascade.login(new UserData("abc", "def",
                "hi")));
    }

    @Test
    @Order(4)
    @DisplayName("Check logout works")
    public void checkLogout() throws ResponseException {
        serverFascade.login(new UserData("abc", "123", "hi"));
        assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(5)
    @DisplayName("Check logout fails")
    public void checkLogoutFails() throws ResponseException {
        serverFascade.logout();
        assertThrows(ResponseException.class, () -> serverFascade.logout());
    }

    @Test
    @Order(6)
    @DisplayName("Check listgame works")
    public void checkListGame() throws ResponseException {
        serverFascade.login(new UserData("abc", "123", "hi"));
        List<GameData> gameDataList = new ArrayList<>();
        if (game1ID < game2ID) {
            gameDataList.add(new GameData(game1ID, null, null, "game1",
                    new ChessGame()));
            gameDataList.add(new GameData(game2ID, null, null, "game2",
                    new ChessGame()));
        } else {
            gameDataList.add(new GameData(game2ID, null, null, "game2",
                    new ChessGame()));
            gameDataList.add(new GameData(game1ID, null, null, "game1",
                    new ChessGame()));
        }
        assertEquals(new ListGameData(gameDataList), serverFascade.listGames());
    }

    @Test
    @Order(7)
    @DisplayName("Check listgame fails well")
    public void checkListGameFails() throws ResponseException {
        serverFascade.clear();
        assertThrows(ResponseException.class, () -> serverFascade.listGames());
    }

    @Test
    @Order(8)
    @DisplayName("Check createGame works")
    public void checkCreateGame() throws ResponseException {
        serverFascade.clear();
        serverFascade.register(new UserData("fed", "123", "hi"));
        int gameID = serverFascade.createGame("abc").gameID();
        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(new GameData(gameID, null, null, "abc", new ChessGame()));
        assertEquals(new ListGameData(gameDataList), serverFascade.listGames());
    }

    @Test
    @Order(9)
    @DisplayName("Check createGame fails well")
    public void checkCreateGameFail() throws ResponseException {
        assertThrows(ResponseException.class, () -> serverFascade.createGame(null));
    }


    @Test
    @Order(10)
    @DisplayName("Check joinGame works")
    public void checkJoinGame() throws ResponseException {
        assertDoesNotThrow(() -> serverFascade.joinGame(new JoinGameRequest("BLACK", game1ID)));
    }

    @Test
    @Order(11)
    @DisplayName("Check joinGame fails")
    public void checkJoinGameFail() throws Exception {
        serverFascade.joinGame(new JoinGameRequest("BLACK", game1ID));
        assertThrows(ResponseException.class, () -> serverFascade.joinGame(new JoinGameRequest("BLACK",
                game1ID)));
    }

    @Test
    @Order(12)
    @DisplayName("Check clear works")
    public void checkClear() throws Exception {
        assertDoesNotThrow(() -> serverFascade.clear());
    }

    @Test
    @Order(13)
    @DisplayName("Check clear fail fine when server is off")
    public void checkClearNoFail() throws Exception {
        server.stop();
        assertThrows(ResponseException.class, () -> serverFascade.clear());
    }

}
