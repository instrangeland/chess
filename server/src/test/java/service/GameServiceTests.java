package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.sql.GameSQL;
import error.TakenError;
import model.GameData;
import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import response.ListGamesResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {
    private static int newGameNum;
    private static int secondGameNum;
    @BeforeAll
    public static void init() {
        GameDAO dao = new GameSQL();
        GameService.setGameDAO(dao);
        GameService.deleteGames();
        newGameNum = service.GameService.newGame("abc").gameID();
        secondGameNum = service.GameService.newGame("def").gameID();

    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(0)
    @DisplayName("Check getGame fail")
    public void getGameFail() throws Exception {
        assertNull(service.GameService.getGame(-1));
    }

    @Test
    @Order(1)
    @DisplayName("Check getGame")
    public void getGameCheck() throws Exception {
        GameData gameData = service.GameService.getGame(newGameNum);
        assertNotNull(gameData);
        assertNull(gameData.blackUsername());
        assertNull(gameData.whiteUsername());
    }

    @Test
    @Order(2)
    @DisplayName("Check getGame")
    public void setGameCheck() throws Exception {
        GameService.deleteGames();
        int newGameNum = GameService.newGame("hello").gameID();
        GameData gameData = service.GameService.getGame(newGameNum);
        GameData newGameData = new GameData(newGameNum, "abc", "def", "hello",
                new ChessGame());
        GameService.updateGame(newGameNum, newGameData);
        gameData = service.GameService.getGame(newGameNum);

        assertEquals(gameData, newGameData);
    }

    @Test
    @Order(3)
    @DisplayName("Check updateGame and getGame")
    public void setGameFailure() throws Exception {
        assertThrows(IndexOutOfBoundsException.class, () -> service.GameService.updateGame(-1,
                new GameData(1234, "abc", "def", "hello", new ChessGame())));

    }

    @Test
    @Order(4)
    @DisplayName("Check listgame")
    public void listGameCheck() throws Exception {
        service.GameService.deleteGames();
        int gameNum = GameService.newGame("abc").gameID();
        int game2 = GameService.newGame("abc").gameID();

        List<GameData> gameDataList = new ArrayList<>();
        if (gameNum < game2) {
            gameDataList.add(GameService.getGame(gameNum));
            gameDataList.add(GameService.getGame(game2));
        } else {
            gameDataList.add(GameService.getGame(game2));
            gameDataList.add(GameService.getGame(gameNum));
        }


        ListGamesResponse gamesResponse = new ListGamesResponse(gameDataList);
        assertEquals(gamesResponse, GameService.listGames());
    }


    @Test
    @Order(5)
    @DisplayName("Check joinGame")
    public void joinGameCheck() throws Exception {
        GameService.deleteGames();
        int gameNum = GameService.newGame("def").gameID();
        GameService.joinGame("abc", "BLACK", gameNum);
        GameData game = GameService.getGame(gameNum);
        assertEquals(game, new GameData(gameNum, null, "abc", "def",
                new ChessGame()));
    }

    @Test
    @Order(6)
    @DisplayName("Check joinGame")
    public void usernameTakenCheck() throws Exception {
        GameService.deleteGames();
        int gameNum = GameService.newGame("def").gameID();
        GameService.joinGame("abc", "BLACK", gameNum);
        assertThrows(TakenError.class, () -> GameService.joinGame("abc", "BLACK", gameNum));
    }









}
