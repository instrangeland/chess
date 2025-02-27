package service;

import dataaccess.AuthDAO;
import dataaccess.AuthRam;
import error.ResponseError;
import error.UnauthorizedError;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.LogoutRequest;

import java.util.Objects;
import java.util.UUID;

public class AuthService {
    public static void setAuthDAO(AuthDAO authDAO) {
        AuthService.authDAO = authDAO;
    }

    static private AuthDAO authDAO;

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
        authDAO.deleteAuth(request.authToken());
    }


}
