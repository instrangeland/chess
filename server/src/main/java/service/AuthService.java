package service;

import dataaccess.AuthDAO;
import dataaccess.AuthRam;

public class AuthService {
    static AuthDAO authDAO = new AuthRam();
    static public void deleteAuths() {
        authDAO.clear();
    }
}
