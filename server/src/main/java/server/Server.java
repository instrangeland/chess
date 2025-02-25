package server;

import handler.*;
import org.eclipse.jetty.server.Authentication;
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
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", clearHandler::handleRequest);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
