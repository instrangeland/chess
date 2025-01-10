package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final int[][] knightOffsets = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    private boolean doubleMovedForAlPassant;
    private boolean hasMoved = false;
    private PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        this.doubleMovedForAlPassant = false;
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

    public static Set<ChessPiece> convertPiecesFromPositions(ChessBoard board, Set<ChessPosition> positions) {
        Set<ChessPiece> pieces = new HashSet<>();
        for (ChessPosition position: positions) {
            pieces.add(board.getPiece(position));
        }
        return pieces;
    }

    public boolean isDoubleMovedForAlPassant() {
        return doubleMovedForAlPassant;
    }

    public void setDoubleMovedForAlPassant(boolean doubleMovedForAlPassant) {
        this.doubleMovedForAlPassant = doubleMovedForAlPassant;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
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

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
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
                moves.add(new ChessMove(myPosition, newPosition, null, false));
                //so we know we can move one, so can we move two?
                newPosition = myPosition.offsetRowBy(direction * 2);
                if (board.getPiece(newPosition) == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null, true));
                }
            }
        } else {
            newPosition = myPosition.offsetRowBy(direction);
            if (!posInBounds(newPosition)) {
                System.out.println("pawn out of bounds. We'll just break here");
                return moves;
            }
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
        moves.addAll(addPawnCaptureMoves(board, color, myPosition, newPosition, promoteRow, direction));
        newPosition = myPosition.offsetPosBy(direction, 1);
        moves.addAll(addPawnCaptureMoves(board, color, myPosition, newPosition, promoteRow, direction));
        return moves;
    }

    private Set<ChessMove> addPawnCaptureMoves(ChessBoard board, ChessGame.TeamColor color, ChessPosition myPosition,
                                               ChessPosition newPosition, int promoteRow, int direction) {
        Set<ChessMove> moves = new HashSet<>();
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
            } else if (board.getPiece(newPosition) == null) {
                //check if square next to you is a pawn
                ChessPosition elPassantCheckPosition = newPosition.offsetRowBy(-direction);
                if (board.getPiece(elPassantCheckPosition) != null) {
                    ChessPiece pieceToCheck = board.getPiece(elPassantCheckPosition);
                    if (pieceToCheck.pieceType == PieceType.PAWN && pieceToCheck.pieceColor != color &&
                            pieceToCheck.doubleMovedForAlPassant) {
                        if (promoteRow == newPosition.getRow()) {
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN, elPassantCheckPosition));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT, elPassantCheckPosition));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK, elPassantCheckPosition));
                            moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP, elPassantCheckPosition));
                        } else {
                            moves.add(new ChessMove(myPosition, newPosition, null, elPassantCheckPosition));
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * checks if (and finds) the valid rook in the direction given
     * @param board Chessboard
     * @param direction either a -1 (left) or a 1 (right)
     * @param pos starting position of king
     * @return ChessPosition (will be null if no rook, rook that has moved, or pieces in way)
     */
    private ChessPosition checkForRookInLineCastling(ChessBoard board, int direction, ChessPosition pos) {
        ChessPosition testPos = pos;
        for (int i = 1; i<5; i++) {
            testPos = pos.offsetColBy(i*direction);
            if (!posInBounds(testPos))
                return null;
            ChessPiece locPiece = board.getPiece(testPos);
            if (locPiece != null) {
                if (locPiece.pieceColor == this.pieceColor &&
                        locPiece.pieceType == PieceType.ROOK && !locPiece.isHasMoved()) {
                    return testPos;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private Set<ChessMove> generateCastlingMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        //left castling
        if (board.getPiece(myPosition).isHasMoved()) { //verify if king has moved no castling
            return moves;
        }
        ChessPosition leftRook = checkForRookInLineCastling(board, -1, myPosition);
        if (leftRook != null) {
            if (leftRook.getColumn() == 1) {
                ChessMove rookMove = new ChessMove(leftRook, myPosition.offsetColBy(-1));
                moves.add(new ChessMove(myPosition, myPosition.offsetColBy(-2), null, rookMove));
            }

        }
        ChessPosition rightRook = checkForRookInLineCastling(board, 1, myPosition);
        if (rightRook != null) {
            if (rightRook.getColumn() == 8) {
                ChessMove rookMove = new ChessMove(rightRook, myPosition.offsetColBy(1));
                moves.add(new ChessMove(myPosition, myPosition.offsetColBy(2), null, rookMove));
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
                moves.addAll(generateCastlingMoves(board, myPosition));
                // check for left rook
                // check for right rook
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
                        if (!(board.getPiece(newPosition)!=null && board.getPiece(newPosition).getTeamColor() == pieceColor)) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
                break;
        }
        //printMoves(moves);
        return moves;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }
}
