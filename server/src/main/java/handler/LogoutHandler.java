package handler;

import com.google.gson.Gson;
import model.AuthData;
import request.LoginRequest;
import request.LogoutRequest;
import service.AuthService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{

    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        String auth = req.headers("authorization");

        LogoutRequest request = new LogoutRequest(auth);
        AuthService.logout(request);
        return "{}";
    }
}
