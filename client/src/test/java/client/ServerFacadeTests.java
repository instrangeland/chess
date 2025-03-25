package client;

import error.ResponseError;
import error.UnauthorizedError;
import exception.ResponseException;
import model.GameData;
import model.ListGameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFascade;
import service.AuthService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFascade serverFascade;
    private static int game1ID;
    private static int game2ID;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url = "http://localhost/"+port;
    }

    @BeforeEach
    public void setup() {
        game1ID = serverFascade.createGame("game1");
        game2ID = serverFascade.createGame("game2");
        serverFascade.register(new UserData("abc", "123", "hi"));
        serverFascade.register(new UserData("def", "123", "hi"));
    }

    @AfterEach
    public void resetServer() {
        serverFascade.login(new UserData("abc", "123", "hi");
        serverFascade.clear();
    }


//    @AfterAll
//    static void stopServer() {
//        server.stop();
//    }




    @Test
    @Order(0)
    @DisplayName("Check register works")
    public void checkRegister() throws Exception {
        assertDoesNotThrow(() -> serverFascade.register("fed", "123", "hi")));
    }

    @Test
    @Order(1)
    @DisplayName("Check register fails with duplicate username")
    public void checkRegisterFails() throws Exception {
        assertThrows(ResponseException.class, () -> serverFascade.register(new UserData("def", "123", "hi")));
    }

    @Test
    @Order(2)
    @DisplayName("Check login works")
    public void checkLogin() throws Exception {
        assertDoesNotThrow(() -> serverFascade.login(new UserData("abc", "123", "hi")));
    }

    @Test
    @Order(3)
    @DisplayName("Check login fails with invalid user")
    public void checkLoginFails() throws Exception {
        assertThrows(ResponseException.class, () -> serverFascade.login(new UserData("abc", "def", "hi"));
    }

    @Test
    @Order(4)
    @DisplayName("Check logout works")
    public void checkLogout() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi");
        assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(5)
    @DisplayName("Check logout fails")
    public void checkLogoutFails() throws Exception {
        assertThrows(ResponseException.class, () -> serverFascade.logout());
    }

    @Test
    @Order(6)
    @DisplayName("Check listgame works")
    public void checkListGame() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi");
        List<GameData> gameDataList = new ArrayList<>();
        if (game1ID < game2ID) {
            gameDataList.add(gameDAO.getGame(game1ID));
            gameDataList.add(gameDAO.getGame(game2ID));
        } else {
            gameDataList.add(gameDAO.getGame(game2ID));
            gameDataList.add(gameDAO.getGame(game1ID));
        }
        assertEquals(ListGameData(gameDataList), serverFascade.listGames());
    }

    @Test
    @Order(7)
    @DisplayName("Check listgame fails well")
    public void checkListGameFails() throws Exception {
        serverFascade.clear();
        assertThrows(ResponseException.class, () -> serverFascade.listGames());
    }

    @Test
    @Order(8)
    @DisplayName("Check createGame works")
    public void checkCreateGame() throws Exception {
        serverFascade.clear();
        serverFascade.register("fed", "123", "hi");
        int gameID = serverFascade.createGame("abc");
        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(gameDAO.getGame(gameID));
        assertEquals(ListGameData(gameDataList), serverFascade.listGames());
    }

    @Test
    @Order(9)
    @DisplayName("Check createGame fails well")
    public void checkCreateGameFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }


    @Test
    @Order(10)
    @DisplayName("Check joinGame works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
                assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(11)
    @DisplayName("Check joinGame fails")
    public void checkClearNoFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }

    @Test
    @Order(12)
    @DisplayName("Check clear works")
    public void checkClear() throws Exception {
        assertDoesNotThrow(() -> () -> serverFascade::clear()));
    }

    @Test
    @Order(13)
    @DisplayName("Check clear fails when server is off")
    public void checkClearNoFail() throws Exception {
        server.stop();
        assertThrows(ResponseException.class, () -> serverFascade::clear());
    }
    
}
