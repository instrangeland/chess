package chess;

import java.util.HashSet;
import java.util.Set;

public class ChessRulebook {
    private final int[][] knightOffsets = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    public static boolean canPositionBeAttacked(ChessGame.TeamColor teamColor, ChessBoard board, ChessPosition position) {

        ChessGame.TeamColor otherTeam = teamColor.oppositeColor();
        Set<ChessPosition> otherTeamPiecesPositions = board.findPiecesPositions(otherTeam);
        Set<ChessMove> otherTeamMoves = new HashSet<>();
        for (ChessPosition chessPosition : otherTeamPiecesPositions) {
            otherTeamMoves.addAll(board.getPiece(chessPosition).pieceMoves(board, chessPosition));
        }
        for (ChessMove otherTeamMove : otherTeamMoves) {
            if (otherTeamMove.getEndPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }



}
