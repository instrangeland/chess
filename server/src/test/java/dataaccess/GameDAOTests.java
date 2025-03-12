package dataaccess;

import dataaccess.*;
import dataaccess.SQL.AuthSQL;
import dataaccess.SQL.GameSQL;
import error.ResponseError;
import error.UnauthorizedError;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import service.AuthService;
import service.UserService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOTests {
    static GameDAO gameDAO;

    @BeforeAll
    public static void init() {
        gameDAO = new GameSQL();
        gameDAO.clear();

    }

    @AfterAll
    public static void clean() {
        gameDAO.clear();

    }

    @Test
    @Order(0)
    @DisplayName("Check createGame")
    public void checkCreateGame() throws Exception {
        int gameNum = gameDAO.createGame("abc");
        GameData data = gameDAO.getGame(gameNum);
        assertEquals("abc", data.gameName());
    }

    @Test
    @Order(0)
    @DisplayName("Check createGame fails")
    public void checkCreateGameFails() throws Exception {
        assertThrows(ResponseError.class, () -> gameDAO.createGame(null));

    }


}
