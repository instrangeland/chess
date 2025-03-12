package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.SQL.GameSQL;
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
        assertThrows(IndexOutOfBoundsException.class, () -> service.GameService.getGame(999));
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
    @DisplayName("Check updateGame and getGame")
    public void setGameCheck() throws Exception {
        GameData gameData = service.GameService.getGame(newGameNum);
        GameData newGameData = new GameData(1234, "abc", "def", "hello",
                new ChessGame());
        service.GameService.updateGame(newGameNum, newGameData);

        gameData = service.GameService.getGame(newGameNum);
        assertEquals(gameData, newGameData);
    }

    @Test
    @Order(3)
    @DisplayName("Check updateGame and getGame")
    public void setGameFailure() throws Exception {
        assertThrows(IndexOutOfBoundsException.class, () -> service.GameService.updateGame(4,
                new GameData(1234, "abc", "def", "hello", new ChessGame())));

    }

    @Test
    @Order(4)
    @DisplayName("Check listgame")
    public void listGameCheck() throws Exception {
        GameData gameData = service.GameService.getGame(newGameNum);
        GameData newGameData = new GameData(1234, "abc", "def", "hello",
                new ChessGame());
        service.GameService.updateGame(newGameNum, newGameData);

        gameData = service.GameService.getGame(newGameNum);
        assertEquals(gameData, newGameData);
        GameData secondGame = GameService.getGame(secondGameNum);

        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(newGameData);
        gameDataList.add(secondGame);


        ListGamesResponse gamesResponse = new ListGamesResponse(gameDataList);
        assertEquals(gamesResponse, GameService.listGames());
    }


    @Test
    @Order(5)
    @DisplayName("Check joinGame")
    public void joinGameCheck() throws Exception {
        GameService.joinGame("abc", "BLACK", secondGameNum);
        GameData game = GameService.getGame(secondGameNum);

        assertEquals(game, new GameData(secondGameNum, null, "abc", "def",
                new ChessGame()));
    }

    @Test
    @Order(6)
    @DisplayName("Check joinGame")
    public void usernameTakenCheck() throws Exception {
        assertThrows(TakenError.class, () -> GameService.joinGame("abc", "BLACK", secondGameNum));
    }









}
