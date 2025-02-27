package server;

import com.google.gson.Gson;
import dataaccess.*;
import error.HttpErrorMessage;
import error.ResponseError;
import error.UnauthorizedError;
import handler.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

//    public static GameService getGameService() {
//        return gameService;
//    }
//
//    public static UserService getUserService() {
//        return userService;
//    }
//
//    public static AuthService getAuthService() {
//        return authService;
//    }
    static private final ClearHandler clearHandler = new ClearHandler();
    static private final JoinGameHandler joinGameHandler = new JoinGameHandler();
    static private final ListGamesHandler listGamesHandler = new ListGamesHandler();
    static private final LoginHandler loginHandler = new LoginHandler();
    static private final LogoutHandler logoutHandler = new LogoutHandler();
    static private final NewGameHandler newGameHandler = new NewGameHandler();
    static private final RegistrationHandler registrationHandler = new RegistrationHandler();

//    static private final AuthService authService = new AuthService();
//    static private final GameService gameService = new GameService();
//    static private final UserService userService = new UserService();

    public int run(int desiredPort) {

        UserDAO userDAO = new UserRam();
        GameDAO gameDAO = new GameRam();
        AuthDAO authDAO = new AuthRam();

        AuthService.setAuthDAO(authDAO);
        GameService.setGameDAO(gameDAO);
        UserService.setUserDAO(userDAO);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);
        Spark.post("/user", registrationHandler::handleRequest);
        Spark.post("/game", newGameHandler::handleRequest);
        Spark.put("/game", joinGameHandler::handleRequest);
        Spark.get("/game", listGamesHandler::handleRequest);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.exception(ResponseError.class, this::responseExceptionHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    private void responseExceptionHandler(ResponseError error, Request req, Response res) {
        res.status(error.getCode());
        HttpErrorMessage message = new HttpErrorMessage(error.getMessage());
        res.body(new Gson().toJson(message));
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
