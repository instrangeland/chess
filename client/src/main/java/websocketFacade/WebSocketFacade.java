package websocketFacade;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    public WebSocketFacade(String url, NotificationHandler notificationHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case ERROR:
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            notificationHandler.error(errorMessage);
                            break;
                        case NOTIFICATION:
                            NotificationMessage notificationMessage =
                                    new Gson().fromJson(message, NotificationMessage.class);
                            notificationHandler.notify(notificationMessage);
                            break;
                        case LOAD_GAME:
                            LoadMessage loadMessage =
                                    new Gson().fromJson(message, LoadMessage.class);
                            notificationHandler.loadGame(loadMessage);
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try {
            ResignCommand command = new ResignCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void move(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            MoveCommand command = new MoveCommand(authToken, gameID, move);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void leave(String authToken, int gameID) throws ResponseException {
        try {
            LeaveCommand command = new LeaveCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
    public void observe(String authToken, int gameID) throws ResponseException {
        try {
            ConnectCommand command = new ConnectCommand(authToken, gameID);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void join(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            ConnectCommand command = new ConnectCommand(authToken, gameID, color);
            send(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }



    public void send(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

}