package handler;

import com.google.gson.Gson;
import model.AuthData;
import request.LoginRequest;
import service.AuthService;
import spark.Request;
import spark.Response;

public class LoginHandler extends Handler{

    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        AuthData data = AuthService.login(request);
        return gson.toJson(data);
    }
}
