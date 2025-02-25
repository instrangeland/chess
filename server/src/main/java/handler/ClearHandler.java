package handler;

import service.AuthService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
    @Override
    public Object handleRequest(Request req, Response res) {
        AuthService.deleteAuths();
        return "{}";
    }
}
