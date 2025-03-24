package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.JoinGameRequest;

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.net.*;
import java.util.List;

public class ServerFascade {
    private final String serverUrl;

    public ServerFascade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException {
        this.makeRequest("DELETE", "/db", null, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        return this.makeRequest("POST", "/user", userData, AuthData.class);
    }

    public AuthData login(UserData userData) throws ResponseException {
        return this.makeRequest("POST", "/session", userData, AuthData.class);
    }

    public void logout() throws ResponseException {
        this.makeRequest("DELETE", "/session", null, null);
    }

    public List<GameData> listGames() {
        return null;
    }

    public GameData createGame(String gameName) {
        return null;
    }

    public void joinGame(JoinGameRequest joinGameRequest) {

    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
