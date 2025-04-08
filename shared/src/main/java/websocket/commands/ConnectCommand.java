package websocket.commands;

import chess.ChessGame;

public class ConnectCommand extends UserGameCommand{
    public ChessGame.TeamColor getColor() {
        return color;
    }

    public boolean isObserve() {
        return observe;
    }

    private boolean observe;

    private ChessGame.TeamColor color;
    public ConnectCommand(String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(CommandType.CONNECT, authToken, gameID);
        this.color = color;
        observe = false;
    }

    public ConnectCommand(String authToken, Integer gameID) {
        super(CommandType.CONNECT, authToken, gameID);
        this.color = null;
        observe = true;
    }
}
