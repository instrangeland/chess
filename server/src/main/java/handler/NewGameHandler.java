package handler;

import com.google.gson.Gson;
import error.BadRequestError;
import error.UnauthorizedError;
import model.AuthData;
import request.NewGameRequest;
import request.RegisterRequest;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class NewGameHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        if (authToken == null) {
            throw new UnauthorizedError();
        }
        AuthService.auth(authToken);
        NewGameRequest request = gson.fromJson(req.body(), NewGameRequest.class);
        if (request.gameName() == null) {
            throw new BadRequestError();
        }
        return gson.toJson(GameService.newGame(request.gameName()));
    }
}
