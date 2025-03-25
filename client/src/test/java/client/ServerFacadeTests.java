package client;

import error.ResponseError;
import error.UnauthorizedError;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFascade;
import service.AuthService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
    public void checkClear() throws Exception {
        assertDoesNotThrow(() -> serverFascade.login(new UserData("abc", "123", "hi")));
    }

    @Test
    @Order(3)
    @DisplayName("Check login fails with invalid user")
    public void checkClearNoFail() throws Exception {
        assertThrows(ResponseException.class, () -> serverFascade.login(new UserData("abc", "def", "hi"));
    }

    @Test
    @Order(4)
    @DisplayName("Check logout works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
        assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(5)
    @DisplayName("Check logout fails")
    public void checkClearNoFail() throws Exception {
        assertThrows(UnauthorizedError.class, serverFascade.logout());
    }

    @Test
    @Order(6)
    @DisplayName("Check listgame works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
                assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(7)
    @DisplayName("Check listgame fails well")
    public void checkClearNoFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }

    @Test
    @Order(8)
    @DisplayName("Check listgame works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
                assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(9)
    @DisplayName("Check listgame fails well")
    public void checkClearNoFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }

    @Test
    @Order(10)
    @DisplayName("Check creategame works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
                assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(11)
    @DisplayName("Check creategame fails")
    public void checkClearNoFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }

    @Test
    @Order(12)
    @DisplayName("Check joinGame works")
    public void checkClear() throws Exception {
        serverFascade.login(new UserData("abc", "123", "hi")
                assertDoesNotThrow(() -> serverFascade.logout());
    }

    @Test
    @Order(13)
    @DisplayName("Check joinGame fails")
    public void checkClearNoFail() throws Exception {
        serverFascade.clear();
        assertDoesNotThrow(serverFascade.logout());
    }





    @Test
    @Order(14)
    @DisplayName("Check clear works")
    public void checkClear() throws Exception {
        assertDoesNotThrow(() -> AuthService.auth(auth.authToken()));
    }

    @Test
    @Order(15)
    @DisplayName("Check clear fails when server is off")
    public void checkClearNoFail() throws Exception {
        server.stop();
        assertThrows(ResponseException.class, () -> serverFascade::clear(););
    }
    
}
