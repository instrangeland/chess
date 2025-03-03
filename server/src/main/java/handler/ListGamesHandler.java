package handler;

import com.google.gson.Gson;
import error.BadRequestError;
import request.NewGameRequest;
import service.AuthService;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        if (authToken == null) {
            throw new BadRequestError();
        }
        AuthService.auth(authToken);

        return gson.toJson(GameService.listGames());
    }
}
