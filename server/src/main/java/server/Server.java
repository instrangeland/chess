package server;

import com.google.gson.Gson;
import dataaccess.*;
import error.HttpErrorMessage;
import error.ResponseError;
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
    static private final ClearHandler CLEAR_HANDLER = new ClearHandler();
    static private final JoinGameHandler JOIN_GAME_HANDLER = new JoinGameHandler();
    static private final ListGamesHandler LIST_GAMES_HANDLER = new ListGamesHandler();
    static private final LoginHandler LOGIN_HANDLER = new LoginHandler();
    static private final LogoutHandler LOGOUT_HANDLER = new LogoutHandler();
    static private final NewGameHandler NEW_GAME_HANDLER = new NewGameHandler();
    static private final RegistrationHandler REGISTRATION_HANDLER = new RegistrationHandler();

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
        Spark.delete("/db", CLEAR_HANDLER::handleRequest);
        Spark.post("/session", LOGIN_HANDLER::handleRequest);
        Spark.delete("/session", LOGOUT_HANDLER::handleRequest);
        Spark.post("/user", REGISTRATION_HANDLER::handleRequest);
        Spark.post("/game", NEW_GAME_HANDLER::handleRequest);
        Spark.put("/game", JOIN_GAME_HANDLER::handleRequest);
        Spark.get("/game", LIST_GAMES_HANDLER::handleRequest);
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
