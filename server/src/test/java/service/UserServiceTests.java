package service;

import dataaccess.*;
import dataaccess.SQL.AuthSQL;
import dataaccess.SQL.UserSQL;
import error.TakenError;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {


    @BeforeAll
    public static void init() {
        UserDAO userDAO = new UserSQL();
        UserService.setUserDAO(userDAO);

        AuthDAO dao = new AuthSQL();
        AuthService.setAuthDAO(dao);

        UserService.registerUser("abc", "123", "hi");
    }

    @BeforeEach
    public void setup() {

    }

    @Test
    @Order(0)
    @DisplayName("Check get user")
    public void checkGetUser() throws Exception {
        UserData data = new UserData("abc", "123", "hi");
        UserData retrievedUser = UserService.getUser("abc");
        assertEquals(data.email(), retrievedUser.email());
        assertTrue(BCrypt.checkpw(data.password(), retrievedUser.password()));
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
        UserData data = new UserData("abc", "123", "hi");
        UserService.registerUser("abc", "123", "hi");
        UserData retrievedUser = UserService.getUser("abc");
        assertEquals(data.email(), retrievedUser.email());
        assertTrue(BCrypt.checkpw(data.password(), retrievedUser.password()));
    }


    @Test
    @Order(4)
    @DisplayName("Check double-register users")
    public void checkDoubleRegister() throws Exception {
        assertThrows(TakenError.class, () -> UserService.registerUser("abc", "123", "abc"));
    }

}