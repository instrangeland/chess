package websocket.commands;

import chess.ChessGame;

public class ResignCommand extends UserGameCommand {
    public ChessGame.TeamColor getColor() {
        return color;
    }
    public ResignCommand(String authToken, Integer gameID) {
        super(CommandType.RESIGN, authToken, gameID);
    }
}
