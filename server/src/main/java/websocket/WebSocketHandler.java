package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.GameDAO;
import exception.ResponseException;
import handler.LoginHandler;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    void sendGame(GameData gameData, String username) {

    }

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private String getUsername(String auth) throws AuthenticationException {
        AuthData authData = authDAO.getAuth(auth);
        if (authData == null) {
            throw new AuthenticationException("Auth token "+auth+" is not valid");
        }
        return authData.username();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String auth = command.getAuthToken();
            String username = getUsername(auth);
            switch (command.getCommandType()) {
                case CONNECT:
                    ConnectCommand connectCommand = new Gson().fromJson(message, ConnectCommand.class);
                    join(username, session, connectCommand);
                    break;
                case MAKE_MOVE:
                    MoveCommand moveCommand = new Gson().fromJson(message, MoveCommand.class);
                    makeMove(auth, moveCommand);
                    break;
                case LEAVE:
                    LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    makeMove(auth, leaveCommand);
                    break;
                case RESIGN:
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    makeMove(auth, resignCommand);
                    break;
            }
        }
        catch (ResponseException e) {

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPlayer(String auth) {
        AuthData data = authDAO.getAuth(auth);
        return !(data == null);
    }

    private void makeErrorMessage(int gameID, String username, String message ) throws IOException {
        String errorJSON = new Gson().toJson(new ErrorMessage(message));
        connections.send(username, gameID, errorJSON);;
    }

    private void join(String username, Session session, ConnectCommand command) throws IOException {
        int gameID = command.getGameID();
        connections.add(username, gameID, session);
        if (!isPlayer(command.getAuthToken())) {
            makeErrorMessage(gameID, username, "Invalid auth token");
            return;
        }

        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            makeErrorMessage(gameID, username, "No such gameID: " + gameID);
            return;
        }
        if (command.isObserve()) {
            NotificationMessage message = new NotificationMessage("Player "+username +
                    " has joined as an observer.");
            connections.broadcast(username, gameID, message);
        } else {
            if (!(Objects.equals(username, getTeamUsername(gameData, command.getColor())))) {
                makeErrorMessage(gameID, username, "Cannot join as " + command.getColor().toString()
                        + ", player "+getTeamUsername(gameData, command.getColor()) + " is that color.");
                return;
            }
            NotificationMessage message = new NotificationMessage("Player "+username +
                    " has joined as team "+ command.getColor().toString() + ".");
            connections.broadcast(username, gameID, message);


        }






    }

    private void resign(String auth, UserGameCommand command) {

    }


    private void exit(String auth, UserGameCommand command) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }

    public void makeMove(String auth, UserGameCommand command) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public String getTeamUsername(GameData gameData, ChessGame.TeamColor color) {
        if (ChessGame.TeamColor.WHITE == color) {
            return gameData.whiteUsername();
        } else {
            return gameData.blackUsername();
        }
    }

}