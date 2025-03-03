package service;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import server.Server;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {
    private static int newGameNum;
    @BeforeAll
    public static void init() {
        GameService.deleteGames();
        newGameNum = service.GameService.newGame("abc").gameID();

    }

    @BeforeEach
    public void setup() {

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




}
