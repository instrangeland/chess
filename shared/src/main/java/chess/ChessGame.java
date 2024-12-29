package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board = new ChessBoard();
    private final TeamColor currentPlayer;

    public ChessGame() {
        board.resetBoard();
        currentPlayer = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Set<ChessMove> moves;
        Set<ChessMove> validMoves = new HashSet<>();
        if (this.board.getPiece(startPosition) == null) return null;
        moves = (Set<ChessMove>) this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        TeamColor thisTeam = this.board.getPiece(startPosition).getTeamColor();
        TeamColor otherTeam = thisTeam.switchColor();
        Set<ChessPosition> otherTeamPieces = this.board.findPiecesPositions(otherTeam);
        ChessBoard testBoard = new ChessBoard();
        for (ChessMove move : moves) {
            ChessPiece[][] boardCopy = this.board.getBoard().clone();
            for (int i = 0; i<boardCopy.length; i++) {
                boardCopy[i] = this.board.getBoard()[i].clone();
            }
            testBoard.setBoard(boardCopy);

            executeMoveNoCheck(move, testBoard);
            if (!boardInCheck(thisTeam, testBoard)) {
                System.out.println(move.toString()+" is invalid");
                validMoves.add(move);
            }
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        attemptMoveOnBoard(move, this.board);
    }

    private void attemptMoveOnBoard(ChessMove move, ChessBoard board) throws InvalidMoveException {

    }

    private void executeMoveNoCheck(ChessMove move, ChessBoard board) {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return boardInCheck(teamColor, this.board);
    }

    public boolean boardInCheck(TeamColor teamColor, ChessBoard board) {
        TeamColor otherTeam = teamColor.switchColor();
        Set<ChessPosition> otherTeamPiecesPositions = this.board.findPiecesPositions(otherTeam);
        Set<ChessMove> otherTeamMoves = new HashSet<>();
        for (ChessPosition chessPosition : otherTeamPiecesPositions) {
            otherTeamMoves.addAll(board.getPiece(chessPosition).pieceMoves(board, chessPosition));
        }
        ChessPosition currentTeamKing = board.findPiecePosition(teamColor, ChessPiece.PieceType.KING);
        for (ChessMove otherTeamMove : otherTeamMoves) {
            if (otherTeamMove.getEndPosition().equals(currentTeamKing)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK;

        public TeamColor switchColor() {
            return this == BLACK ? WHITE : BLACK;
        }
    }
}
