package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.GameDAO;
import exception.ResponseException;
import handler.LoginHandler;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;


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
                case CONNECT -> join(username, session, command);
                case OBSERVE -> observe(auth, command);
                case MAKE_MOVE -> makeMove(auth, command);
                case LEAVE -> exit(auth);
                case RESIGN -> resign(auth);
            }
        }
        catch (ResponseException e) {

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPlayer(String auth) {

    }

    private void join(String username, Session session, UserGameCommand command) throws IOException {


        int gameID = command.getGameID();
        connections.add(username, gameID, session);


    }

    private void resign(String auth, UserGameCommand command) {

    }

    private void observe(String auth, UserGameCommand command) {

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
}