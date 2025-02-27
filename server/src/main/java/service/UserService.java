package service;

import dataaccess.UserDAO;
import dataaccess.UserRam;
import error.TakenError;
import model.AuthData;
import model.UserData;
import request.LoginRequest;

public class UserService {
    private static UserDAO userDAO = new UserRam();
    static void deleteUsers() {

    }
    public static UserData getUser(String username) {
        return userDAO.getUser(username);
    }
    public static UserData createUser(String username, String password, String email) {
        return userDAO.createUser(username, password, email);
    }
    public static void createUser(UserData userData) {
        userDAO.createUser(userData);
    }

    public static AuthData registerUser(String username, String password, String email) throws TakenError {
        UserData userData = getUser(username);
        if (userData != null) {
            throw new TakenError();
        }

        createUser(username, password, email);
        return AuthService.login(new LoginRequest(username, password));
    }
}
