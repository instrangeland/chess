package handler;

import spark.Request;
import spark.Response;

abstract class Handler {
    abstract public Object handleRequest(Request req, Response res);

}
