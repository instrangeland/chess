package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.ListGameData;
import model.UserData;
import request.JoinGameRequest;
import java.lang.reflect.Type;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFascade {
    private String token;
    private final String serverUrl;

    public ServerFascade(String url) {
        serverUrl = url;
    }

    public void clear() throws ResponseException {
        this.makeRequest("DELETE", "/db", null, null);
    }

    public AuthData register(UserData userData) throws ResponseException {
        AuthData data = this.makeRequest("POST", "/user", userData, AuthData.class);
        if (data != null) {
            this.token = data.authToken();
        }
        return data;
    }

    public AuthData login(UserData userData) throws ResponseException {
        AuthData data = this.makeRequest("POST", "/session", userData, AuthData.class);
        if (data != null) {
            this.token = data.authToken();
        }
        return data;
    }

    public void logout() throws ResponseException {
        this.makeRequest("DELETE", "/session", null, null);
    }

    public ListGameData listGames() throws ResponseException {
        Type gameList = new TypeToken<List<GameData>>() {}.getType();
        return this.makeRequest("GET", "/game", null, ListGameData.class);
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
            if (token != null) {
                http.setRequestProperty("authorization", token);
            }
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
