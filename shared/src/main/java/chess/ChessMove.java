package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;
    private boolean wouldCauseEnPassantable = false;

    public ChessMove getCastlingRookMove() {
        return castlingRookMove;
    }

    public void setCastlingRookMove(ChessMove castlingRookMove) {
        this.castlingRookMove = castlingRookMove;
    }

    private ChessMove castlingRookMove = null;
    public ChessPosition getEnPassantPieceToKill() {
        return enPassantPieceToKill;
    }

    public void setEnPassantPieceToKill(ChessPosition enPassantPieceToKill) {
        this.enPassantPieceToKill = enPassantPieceToKill;
    }

    private ChessPosition enPassantPieceToKill;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece, ChessPosition enPassantPieceToKill) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=promotionPiece;
        this.enPassantPieceToKill = enPassantPieceToKill;
        this.wouldCauseEnPassantable = false;
        this.castlingRookMove = null;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=null;
        this.enPassantPieceToKill = null;
        this.wouldCauseEnPassantable = false;
        this.castlingRookMove = null;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=promotionPiece;
        this.enPassantPieceToKill = null;
        this.wouldCauseEnPassantable = false;
        this.castlingRookMove = null;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece, boolean willCauseEnPassant) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=promotionPiece;
        this.enPassantPieceToKill = null;
        this.wouldCauseEnPassantable = willCauseEnPassant;
        this.castlingRookMove = null;
    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece,
                     ChessMove castlingRookMove) {
        this.startPosition=startPosition;
        this.endPosition=endPosition;
        this.promotionPiece=null; //look, this is here just for the fact that I can't change the test code. It *really* likes nulls.
        this.enPassantPieceToKill = null;
        this.wouldCauseEnPassantable = false;
        this.castlingRookMove = castlingRookMove;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public boolean isWouldCauseEnPassantable() {
        return wouldCauseEnPassantable;
    }

    public void setWouldCauseEnPassantable(boolean wouldCauseEnPassantable) {
        this.wouldCauseEnPassantable = wouldCauseEnPassantable;
    }
}
