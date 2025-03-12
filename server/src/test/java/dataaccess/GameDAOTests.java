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
import service.GameService;
import service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
    @Order(1)
    @DisplayName("Check createGame fails")
    public void checkCreateGameFails() throws Exception {
        assertThrows(ResponseError.class, () -> gameDAO.createGame(null));
    }

    @Test
    @Order(2)
    @DisplayName("Check getGame")
    public void checkGetGame() throws Exception {
        int gameNum = gameDAO.createGame("abc");
        System.out.println(gameNum);
        GameData data = gameDAO.getGame(gameNum);
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
        assertDoesNotThrow(() -> gameDAO.updateGameData(1, new GameData(1, null,
                null, "abc", new ChessGame())));
    }

    @Test
    @Order(5)
    @DisplayName("Check updateGameData fails")
    public void checkUpdateGameDataFails() throws Exception {
        assertThrows(IndexOutOfBoundsException.class, () -> gameDAO.updateGameData(10,
                new GameData(1, null,null, "abc", new ChessGame())));
    }

    @Test
    @Order(6)
    @DisplayName("Check listGames")
    public void checkListGames() throws Exception {
        GameData gameData = gameDAO.getGame(1);
        gameDAO.createGame("abc");
        GameData newGameData = new GameData(2, "abc", "def", "hello",
                new ChessGame());
        gameDAO.updateGameData(2, newGameData);
        GameData thirdGame = gameDAO.getGame(3);

        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(gameData);
        gameDataList.add(newGameData);
        gameDataList.add(thirdGame);
        assertEquals(gameDataList, gameDAO.listGames());
    }

    @Test
    @Order(7)
    @DisplayName("Check listGames fails")
    public void checkListGamesFails() throws Exception {
        gameDAO.clear();
        assertEquals(new ArrayList<>(), gameDAO.listGames());
    }





}
