package dataaccess;

import dataaccess.*;
import dataaccess.RAM.AuthRam;
import dataaccess.RAM.UserRam;
import dataaccess.SQL.AuthSQL;
import error.ResponseError;
import error.UnauthorizedError;
import model.AuthData;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.LogoutRequest;
import service.AuthService;
import service.UserService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UserDAOTests {
}
