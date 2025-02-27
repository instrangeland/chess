package handler;

import com.google.gson.Gson;
import error.BadRequestError;
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
        System.out.println("making register");
        if (request.username() == null || request.password() == null || request.email() == null) {
            System.out.println("Invalid request.");
            throw new BadRequestError();
        }
        AuthData authData = UserService.registerUser(request.username(), request.password(), request.email());
        System.out.println("Register's auth: "+authData.authToken());
        return gson.toJson(authData);
    }
}
