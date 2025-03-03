package handler;

import com.google.gson.Gson;
import error.BadRequestError;
import error.TakenError;
import error.UnauthorizedError;
import model.GameData;
import request.JoinGameRequest;
import request.NewGameRequest;
import service.AuthService;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{

    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        if (authToken == null) {
            throw new UnauthorizedError();
        }
        AuthService.auth(authToken);
        String username = AuthService.getUsername(authToken);
        JoinGameRequest joinGameRequest = gson.fromJson(req.body(), JoinGameRequest.class);
        if (joinGameRequest.playerColor() == null) {
            throw new BadRequestError();
        }
        if (!joinGameRequest.playerColor().equals("BLACK") && !joinGameRequest.playerColor().equals("WHITE")) {
            throw new BadRequestError();
        }
        try {
            GameData data = GameService.getGame(joinGameRequest.gameID());
            if (data == null) {
                throw new BadRequestError();
            }
            GameData newData;
            if (joinGameRequest.playerColor().equals("BLACK")) {
                if (data.blackUsername() == null) {
                    newData = new GameData(data.gameID(), data.whiteUsername(), username, data.gameName(), data.game());
                } else {
                    throw new TakenError();
                }
            } else {
                if (data.whiteUsername() == null) {
                    newData = new GameData(data.gameID(), username, data.blackUsername(), data.gameName(), data.game());
                } else {
                    throw new TakenError();
                }
            }
            GameService.updateGame(joinGameRequest.gameID(), newData);
        } catch (IndexOutOfBoundsException e) {
            throw new BadRequestError();
        }


        return "{}";
    }
}
