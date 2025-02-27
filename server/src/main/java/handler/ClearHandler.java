package handler;

import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) {
        AuthService.deleteAuths();
        GameService.deleteGames();
        UserService.deleteUsers();

        return "{}";
    }
}
