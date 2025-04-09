package websocket.commands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand{
    private ChessGame.TeamColor color;
    public ChessGame.TeamColor getColor() {
        return color;
    }
    public ResignCommand(String authToken, Integer gameID, ChessGame.TeamColor color) {
        super(CommandType.RESIGN, authToken, gameID);
        this.color = color;
    }
}
