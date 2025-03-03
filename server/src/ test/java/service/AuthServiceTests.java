package service;

import chess.ChessGame;
import dataaccess.*;
import error.TakenError;
import error.UnauthorizedError;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import passoff.model.*;
import passoff.server.TestServerFacade;
import request.LoginRequest;
import request.LogoutRequest;
import response.ListGamesResponse;
import server.Server;

import java.net.HttpURLConnection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTests {

    @BeforeAll

    static AuthData auth;
    public static void init() {
        UserDAO userDAO = new UserRam();
        UserService.setUserDAO(userDAO);

        UserService.registerUser("abc", "xyz", "hi");

        AuthDAO dao = new AuthRam();
        AuthService.setAuthDAO(dao);
        auth = AuthService.login(new LoginRequest("abc", "xyz"));
    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(0)
    @DisplayName("Check auth works")
    public void checkAuth() throws Exception {
        AuthService.auth(auth.authToken());
    }

    @Test
    @Order(1)
    @DisplayName("Check auth fails")
    public void checkAuthFails() throws Exception {
        assertThrows(UnauthorizedError.class, () -> AuthService.auth("abc"));
    }

    @Test
    @Order(2)
    @DisplayName("Check getUsername works")
    public void checkGetUsername() throws Exception {
        assertEquals("abc", AuthService.getUsername(auth.authToken()));
    }

    @Test
    @Order(3)
    @DisplayName("Check getUsername fails well")
    public void checkGetUsernameFails() throws Exception {
        assertNull(AuthService.getUsername("abc"));
    }

    @Test
    @Order(4)
    @DisplayName("Check delete auths")
    public void checkDeleteAuths() throws Exception {
        AuthService.deleteAuths();
        assertThrows(UnauthorizedError.class, () -> AuthService.auth(auth.authToken()));
    }

    @Test
    @Order(5)
    @DisplayName("Check login")
    public void checkLogin() throws Exception {
        auth = AuthService.login(new LoginRequest("abc", "xyz"));
        assertNotNull(auth);
    }

    @Test
    @Order(6)
    @DisplayName("Check login fails")
    public void checkLoginFails() throws Exception {
        assertThrows(UnauthorizedError.class, () -> auth = AuthService.login(new LoginRequest("abc",
                "dbf")));
    }

    @Test
    @Order(5)
    @DisplayName("Check logout")
    public void checkLogout() throws Exception {
        AuthService.logout(new LogoutRequest(auth.authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("Check login fails")
    public void checkLogoutFails() throws Exception {
        assertThrows(UnauthorizedError.class, () -> AuthService.logout(new LogoutRequest(auth.authToken())));
    }







}