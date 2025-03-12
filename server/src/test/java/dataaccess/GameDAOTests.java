package dataaccess;

import chess.ChessGame;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameDAOTests {
    static GameDAO gameDAO;
    private int gameNum;
    private int gameNum2;
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
        gameNum = gameDAO.createGame("abc");
        GameData data = gameDAO.getGame(gameNum);
        assertEquals("abc", data.gameName());
    }

    @Test
    @Order(1)
    @DisplayName("Check createGame fails")
    public void checkCreateGameFails() throws Exception {
        assertThrows(ResponseError.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(2)
    @DisplayName("Check getGame")
    public void checkGetGame() throws Exception {
        gameNum2 = gameDAO.createGame("abc");
        System.out.println(gameNum2);
        GameData data = gameDAO.getGame(gameNum2);
        assertEquals("abc", data.gameName());
    }

    @Test
    @Order(3)
    @DisplayName("Check getGame fails")
    public void checkGetGameFails() throws Exception {
        assertNull(gameDAO.getGame(10));
    }

    @Test
    @Order(4)
    @DisplayName("Check updateGameData")
    public void checkUpdateGameData() throws Exception {
        gameDAO.clear();
        int gameNum = gameDAO.createGame("abc");
        assertDoesNotThrow(() -> gameDAO.updateGameData(gameNum, new GameData(gameNum, null,
                null, "abc", new ChessGame())));
    }

    @Test
    @Order(5)
    @DisplayName("Check updateGameData fails")
    public void checkUpdateGameDataFails() throws Exception {
        assertThrows(IndexOutOfBoundsException.class, () -> gameDAO.updateGameData(10,
                new GameData(gameNum, null,null, "abc", new ChessGame())));
    }

    @Test
    @Order(6)
    @DisplayName("Check listGames")
    public void checkListGames() throws Exception {
        gameDAO.clear();
        int gameNum = gameDAO.createGame("abc");
        int game2 = gameDAO.createGame("abc");
        List<GameData> gameDataList = new ArrayList<>();
        if (gameNum < game2) {
            gameDataList.add(gameDAO.getGame(gameNum));
            gameDataList.add(gameDAO.getGame(game2));
        } else {
            gameDataList.add(gameDAO.getGame(game2));
            gameDataList.add(gameDAO.getGame(gameNum));
        }
        assertEquals(gameDataList, gameDAO.listGames());
    }

    @Test
    @Order(7)
    @DisplayName("Check listGames fails")
    public void checkListGamesFails() throws Exception {
        gameDAO.clear();
        assertEquals(new ArrayList<>(), gameDAO.listGames());
    }

    @Test
    @Order(8)
    @DisplayName("Check clear")
    public void checkClear() throws Exception {
        gameDAO.createGame("abc");
        gameDAO.createGame("abc");
        gameDAO.createGame("abc");
        gameDAO.clear();
        assertEquals(new ArrayList<>(), gameDAO.listGames());
    }

    @Test
    @Order(9)
    @DisplayName("Check clear doesn't blow up when empty")
    public void checkClearFails() throws Exception {
        assertDoesNotThrow(gameDAO::clear);
    }





}
