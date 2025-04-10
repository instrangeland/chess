package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    void sendGame(GameData gameData, int gameID, String username) throws IOException {
        LoadMessage message = new LoadMessage(gameData);
        System.out.println("sending game to "+username);
        String json = new Gson().toJson(message);
        connections.send(username, gameID, json);
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
                    makeMove(username, moveCommand);
                    break;
                case LEAVE:
                    LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    exit(username, leaveCommand);
                    break;
                case RESIGN:
                    ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                    resign(username, resignCommand);
                    break;
            }
        }
        catch (AuthenticationException | ResponseException e) {
            String errorMessage = new Gson().toJson(new ErrorMessage(e.getMessage()));
            session.getRemote().sendString(errorMessage);
        }
    }

    private boolean isPlayer(String auth) {
        AuthData data = authDAO.getAuth(auth);
        return !(data == null);
    }

    private void makeErrorMessage(int gameID, String username, String message ) throws IOException {
        String errorJSON = new Gson().toJson(new ErrorMessage("Error: "+message));
        connections.send(username, gameID, errorJSON);
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

        String blackUser = getTeamUsername(gameData, ChessGame.TeamColor.BLACK);
        String whiteUser = getTeamUsername(gameData, ChessGame.TeamColor.WHITE);

        if (!username.equals(blackUser) && !username.equals(whiteUser)) {
            NotificationMessage message = new NotificationMessage("Player "+username +
                    " has joined as an observer.");
            connections.broadcast(username, gameID, message);
        } else {
            NotificationMessage message;
            if (username.equals(whiteUser)) {
                message = new NotificationMessage("Player " + username +
                        " has joined as white team .");
            } else {
                message = new NotificationMessage("Player " + username +
                        " has joined as black team .");
            }
            connections.broadcast(username, gameID, message);
        }
        sendGame(gameData, gameID, username);
    }

    private void resign(String username, ResignCommand command) throws IOException {
        int gameID = command.getGameID();
        if (!isPlayer(command.getAuthToken())) {
            makeErrorMessage(gameID, username, "Invalid auth token");
            return;
        }
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            makeErrorMessage(gameID, username, "No such gameID: " + gameID);
            return;
        }
        if (!username.equals(getTeamUsername(gameData, ChessGame.TeamColor.WHITE)) &&
                !username.equals(getTeamUsername(gameData, ChessGame.TeamColor.BLACK)) ) {
            makeErrorMessage(gameID, username, "You are not a player");
            return;
        }
        if (gameData.game().isGameDone()) {
            makeErrorMessage(gameID, username, "The game is already done!");
            return;
        }
        System.out.println(username+ " is resigning");
        if (username.equals(getTeamUsername(gameData, ChessGame.TeamColor.WHITE))) {
            gameData.game().resign(ChessGame.TeamColor.WHITE);
        } else {
            gameData.game().resign(ChessGame.TeamColor.BLACK);
        }
        connections.broadcast(username, gameID, new NotificationMessage(username + " resigns!"));
        connections.send(username, gameID, new Gson().toJson(new NotificationMessage("you resigned")));
        gameDAO.updateGameData(gameID, gameData);
    }


    private void exit(String username, UserGameCommand command) throws IOException {
        int gameID = command.getGameID();
        if (!isPlayer(command.getAuthToken())) {
            makeErrorMessage(gameID, username, "Invalid auth token");
            return;
        }
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            makeErrorMessage(gameID, username, "No such gameID: " + gameID);
            return;
        }
        String blackUser = getTeamUsername(gameData, ChessGame.TeamColor.BLACK);
        String whiteUser = getTeamUsername(gameData, ChessGame.TeamColor.WHITE);
        if (username.equals(blackUser)) {
            GameData newGameData = new GameData(gameID, gameData.whiteUsername(), null,
                    gameData.gameName(), gameData.game());
            gameDAO.updateGameData(gameID, newGameData);
        } else if (username.equals(whiteUser)) {
            GameData newGameData = new GameData(gameID, null, gameData.blackUsername(),
                    gameData.gameName(), gameData.game());
            gameDAO.updateGameData(gameID, newGameData);
        }
        connections.remove(username, gameID);
        connections.broadcast(username, gameID, new NotificationMessage("Player "+username+ " left the game."));
    }

    public void setDAOs(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public void makeMove(String username, MoveCommand command) throws ResponseException, IOException {
        int gameID = command.getGameID();
        if (!isPlayer(command.getAuthToken())) {
            makeErrorMessage(gameID, username, "Invalid auth token");
            return;
        }
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            makeErrorMessage(gameID, username, "No such gameID: " + gameID);
            return;
        }
        ChessGame game = gameData.game();
        if (game.isGameDone()) {
            makeErrorMessage(gameID, username, "The game is over! No more moves!");
            return;
        }

        ChessGame.TeamColor color = game.getTeamTurn();
        System.out.println("now it's "+color +" turn");

        if (!username.equals(getTeamUsername(gameData, color))) {
            makeErrorMessage(gameID, username, "It is "+getTeamUsername(gameData, color)+"'s turn, not yours");
            return;
        }
        try {
            game.makeMove(command.getMove());
            System.out.println(username+" made move. Current turn is "+color);
            connections.broadcast(username, gameID, new NotificationMessage("Player "+username+" made move "
                    +command.getMove()));
            connections.broadcast(username, gameID, new LoadMessage(gameData));
            sendGame(gameData, gameID, username);
            System.out.println(username+" made move");
            gameDAO.updateGameData(gameID, gameData);
        }
        catch (InvalidMoveException e) {
            System.out.println("invald move");
            if (game.isInCheck(color)) {
                makeErrorMessage(gameID, username, "The game would still be in check with that move");
            } else {
                makeErrorMessage(gameID, username, "Invalid move!");
            }
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