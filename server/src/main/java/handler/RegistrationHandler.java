package handler;

import com.google.gson.Gson;
import model.AuthData;
import request.LoginRequest;
import request.RegisterRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegistrationHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        res.type("application/json");
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        AuthData authData = UserService.registerUser(request.username(), request.password(), request.email());
        return gson.toJson(authData);
    }
}
