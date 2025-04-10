package dataaccess;

import dataaccess.sql.UserSQL;
import error.ResponseError;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDAOTests {

    static UserDAO userDAO;

    @BeforeAll
    public static void init() {
        userDAO = new UserSQL();
        userDAO.clear();

    }

    @AfterAll
    public static void clean() {
        userDAO.clear();
    }


    @Test
    @Order(0)
    @DisplayName("Check createUser")
    public void checkCreateUser() throws Exception {
        UserData data = userDAO.createUser("abc", "123", "555");
        UserData returnedData = userDAO.getUser(data.username());
        assertEquals(returnedData.email(), data.email());
        assertTrue(BCrypt.checkpw("123", returnedData.password()));
    }

    @Test
    @Order(0)
    @DisplayName("Check createUser breaks")
    public void checkCreateUserBreaks() throws Exception {
        assertThrows(ResponseError.class, () -> userDAO.createUser(null, null, null));
    }



}
