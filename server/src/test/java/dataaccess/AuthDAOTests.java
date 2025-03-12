package dataaccess;

import dataaccess.sql.AuthSQL;
import error.ResponseError;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthDAOTests {
    static AuthDAO authDAO;

    @BeforeAll
    public static void init() {
        authDAO = new AuthSQL();
        authDAO.clear();

    }

    @AfterAll
    public static void clean() {
        authDAO.clear();

    }

    @Test
    @Order(0)
    @DisplayName("Check createauth")
    public void checkCreateAuth() throws Exception {
        AuthData data = authDAO.createAuth("abc");
        AuthData returnedData = authDAO.getAuth(data.authToken());
        assertEquals(returnedData, data);
    }

    @Test
    @Order(1)
    @DisplayName("Check passing null fails")
    public void checkCreateAuthFails() throws Exception {

        assertThrows(ResponseError.class, () -> authDAO.createAuth(null));
    }

    @Test
    @Order(2)
    @DisplayName("Check getAuth")
    public void checkGetAuth() throws Exception {
        AuthData data = authDAO.createAuth("123");
        AuthData returnedData = authDAO.getAuth(data.authToken());
        assertEquals(returnedData, data);
    }

    @Test
    @Order(3)
    @DisplayName("Check getAuth fails")
    public void checkGetAuthFails() throws Exception {
        AuthData data = authDAO.createAuth("123");
        AuthData returnedData = authDAO.getAuth(data.authToken());
        assertNull(authDAO.getAuth("abc123"));
    }

    @Test
    @Order(4)
    @DisplayName("Check deleteAuth")
    public void checkDeleteAuth() throws Exception {
        AuthData data = authDAO.createAuth("123");
        authDAO.deleteAuth(data.authToken());
        AuthData returnedData = authDAO.getAuth(data.authToken());
        assertNull(returnedData);
    }

    @Test
    @Order(5)
    @DisplayName("Check deleteFromAuth fails right")
    public void checkDeleteAuthFails() throws Exception {
        assertThrows(ResponseError.class, () -> authDAO.deleteAuth(null));
    }

    @Test
    @Order(5)
    @DisplayName("Check clear fails right")
    public void checkClear() throws Exception {
        assertThrows(ResponseError.class, () -> authDAO.deleteAuth(null));
    }

    @Test
    @Order(6)
    void testClearWorks() {
        Assertions.assertDoesNotThrow(authDAO::clear);
    }



}
