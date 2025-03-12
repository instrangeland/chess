package service;

import dataaccess.*;

import dataaccess.sql.AuthSQL;
import dataaccess.sql.UserSQL;
import error.UnauthorizedError;
import model.AuthData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTests {

    static AuthData auth;

    @BeforeAll
    public static void init() {
        UserDAO userDAO = new UserSQL();
        UserService.setUserDAO(userDAO);



        AuthDAO dao = new AuthSQL();
        AuthService.setAuthDAO(dao);
        UserService.deleteUsers();
        auth = UserService.registerUser("abc", "xyz", "hi");

    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(0)
    public void checkAuth() throws Exception {
        assertDoesNotThrow(() -> AuthService.auth(auth.authToken()));
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
        assertThrows(NullPointerException.class, () -> AuthService.getUsername("abc"));
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
    @DisplayName("Check logout fails")
    public void checkLogoutFails() throws Exception {
        assertThrows(UnauthorizedError.class, () -> AuthService.logout(new LogoutRequest(auth.authToken())));
    }







}