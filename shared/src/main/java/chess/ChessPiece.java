package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private int[][] knightOffsets = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    private final ChessGame.TeamColor pieceColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    private Set<ChessMove> generatePossibleVerticalMoves(ChessBoard board, int maxLen, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        return moves;
    }

    private Set<ChessMove> generatePossibleHorizontalMoves(ChessBoard board, int maxLen, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        return moves;
    }

    public static boolean posInBounds(int row, int col) {
        return (row < 9 && row > 0 && col < 9 && col > 0);
    }

    public static boolean posInBounds(ChessPosition chessPosition) {
        int row = chessPosition.getRow();
        int col = chessPosition.getColumn();
        return (row < 9 && row > 0 && col < 9 && col > 0);
    }

    public static void printMoves(Set<ChessMove> moves) {
        for (ChessMove move:moves) {
            System.out.print(move.getEndPosition().toString()+", ");
        }
        System.out.println();
    }
    private Set<ChessMove> generatePossibleDiagonalMoves(ChessBoard board, int maxLen, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }
        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }
        for (int i = 1; i <= maxLen; i++) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (posInBounds(newPosition)) {
                if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor ==pieceColor) {
                    break;
                }
                moves.add(new ChessMove(myPosition, newPosition, null));
                if (board.getPiece(newPosition) != null) {
                    break;
                }
            }
        }

        return moves;
    }

    private Set<ChessMove> generatePossiblePawnMoves(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        int startingRow, direction, promoteRow;
        if (this.pieceColor == ChessGame.TeamColor.BLACK) {
            startingRow = 7;
            direction = -1;
            promoteRow = 1;
        } else {
            startingRow = 2;
            direction = 1;
            promoteRow = 8;
        }
        ChessPosition newPosition;
        if (myPosition.getRow() == startingRow) { //so the piece hasn't moved yet.
            newPosition = myPosition.offsetRowBy(direction);
            if (board.getPiece(newPosition) == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
                //so we know we can move one, so can we move two?
                newPosition = myPosition.offsetRowBy(direction * 2);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        } else {
            newPosition = myPosition.offsetRowBy(direction);
            assert posInBounds(newPosition);
            if (board.getPiece(newPosition) == null) {
                if (promoteRow == newPosition.getRow()) {
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        newPosition = myPosition.offsetPosBy(direction, -1);
        if (posInBounds(newPosition)) {
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor != pieceColor) {
                if (promoteRow == newPosition.getRow()) {
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        newPosition = myPosition.offsetPosBy(direction, 1);
        if (posInBounds(newPosition)) {
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).pieceColor != pieceColor) {
                if (promoteRow == newPosition.getRow()) {
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return moves;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        switch (this.pieceType) {
            case KING:
                moves = generatePossibleHorizontalMoves(board, 1, myPosition);
                moves.addAll(generatePossibleVerticalMoves(board, 1, myPosition));
                moves.addAll(generatePossibleDiagonalMoves(board, 1, myPosition));
                break;
            case PAWN:
                moves = generatePossiblePawnMoves(board, this.pieceColor, myPosition);
                break;
            case ROOK:
                moves = generatePossibleHorizontalMoves(board, 8, myPosition);
                moves.addAll(generatePossibleVerticalMoves(board, 8, myPosition));
                break;
            case QUEEN:
                moves = generatePossibleHorizontalMoves(board, 8, myPosition);
                moves.addAll(generatePossibleVerticalMoves(board, 8, myPosition));
                moves.addAll(generatePossibleDiagonalMoves(board, 8, myPosition));
                break;
            case BISHOP:
                moves = generatePossibleDiagonalMoves(board, 8, myPosition);
                break;
            case KNIGHT:
                moves = new HashSet<>();
                for (int[] offset : knightOffsets) {

                    int newRow = myPosition.getRow() + offset[0];
                    int newCol = myPosition.getColumn() + offset[1];

                    ChessPosition newPosition = new ChessPosition(newRow, newCol);

                    // Check if the new position is within the bounds of the board
                    if (posInBounds(newPosition)) {
                        if (!(board.getPiece(newPosition)!=null && board.getPiece(newPosition).getTeamColor() == pieceColor))
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                break;
        }
        //printMoves(moves);
        return moves;
    }
}
