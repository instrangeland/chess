package websocket.commands;

import chess.ChessMove;

public class MoveCommand extends UserGameCommand{
    public ChessMove getMove() {
        return move;
    }

    private final ChessMove move;
    public MoveCommand(String authToken, Integer gameID, ChessMove move) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;

    }
}
