package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.GameRam;
import dataaccess.UserDAO;
import dataaccess.UserRam;
import error.TakenError;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import response.ListGamesResponse;
import server.Server;

import java.net.HttpURLConnection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {


    @BeforeAll
    public static void init() {
        UserDAO userDAO = new UserRam();
        UserService.setUserDAO(userDAO);

        UserService.registerUser("abc", "123", "hi");
    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(0)
    @DisplayName("Check get user")
    public void checkGetUser() throws Exception {
        assertEquals(new UserData("abc", "123", "hi"), UserService.getUser("abc"));
    }

    @Test
    @Order(1)
    @DisplayName("Check get user fails")
    public void checkGetUserFails() throws Exception {
        assertNull(UserService.getUser("def"));
    }

    @Test
    @Order(2)
    @DisplayName("Check clear users")
    public void clearUsers() throws Exception {
        UserService.deleteUsers();
        assertNull(UserService.getUser("abc"));
    }

    @Test
    @Order(3)
    @DisplayName("Check register users")
    public void checkRegisterUser() throws Exception {
        UserService.registerUser("abc", "123", "abc");
        assertEquals(new UserData("abc", "123", "abc"), UserService.getUser("abc"));
        UserService.registerUser("def", "123", "abc");
        assertEquals(new UserData("def", "123", "abc"), UserService.getUser("abc"));
    }


    @Test
    @Order(4)
    @DisplayName("Check double-register users")
    public void checkDoubleRegister() throws Exception {
        assertThrows(TakenError.class, () -> UserService.registerUser("abc", "123", "abc"));
    }

}