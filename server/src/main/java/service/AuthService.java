package service;

import dataaccess.AuthDAO;
import dataaccess.AuthRam;
import dataaccess.DataAccessException;
import error.ResponseError;
import error.UnauthorizedError;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;

import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class AuthService {
    public static void setAuthDAO(AuthDAO authDAO) {
        AuthService.authDAO = authDAO;
    }

    static private AuthDAO authDAO;

    static public void auth(String authToken) throws UnauthorizedError {
        if (AuthService.authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedError();
        }
    }

    static public String getUsername(String authToken) {
        return authDAO.getAuth(authToken).username();
    }

    static public void deleteAuths() {
        authDAO.clear();
    }

    static public AuthData login(LoginRequest request) throws ResponseError {
        UserData user = UserService.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedError();
        } else if (!Objects.equals(request.password(), user.password())) {
            throw new UnauthorizedError();
        }
        return authDAO.createAuth(user.username());
    }

    static public void logout(LogoutRequest request) throws ResponseError {
        AuthData auth = authDAO.getAuth(request.authToken());
        if (auth == null) {
            throw new UnauthorizedError();
        }
        try {
            authDAO.deleteAuth(request.authToken());
        } catch (DataAccessException | SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }


}
