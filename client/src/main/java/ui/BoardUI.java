package ui;

import chess.*;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

public class BoardUI {
    public enum Perspective {
        WHITE,
        BLACK,
        OBSERVER
    };

    static Map<ChessPiece.PieceType, String> pieceToStringMap = new HashMap<>();

    static {
        pieceToStringMap.put(ChessPiece.PieceType.PAWN, "P");
        pieceToStringMap.put(ChessPiece.PieceType.ROOK, "R");
        pieceToStringMap.put(ChessPiece.PieceType.BISHOP, "B");
        pieceToStringMap.put(ChessPiece.PieceType.KNIGHT, "N");
        pieceToStringMap.put(ChessPiece.PieceType.QUEEN, "Q");
        pieceToStringMap.put(ChessPiece.PieceType.KING, "K");
    }

    static void drawBoard(ChessGame game, Perspective perspective, ChessPosition position) {
        ChessBoard board = game.getBoard();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        boolean reverse = (perspective == Perspective.BLACK);
        printLetterHeader(out, reverse);
        printGame(out, reverse, game, position);
        printLetterHeader(out, reverse);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
        if (game.isGameDone()) {
            System.out.println("Game is done");
        }
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            System.out.println("White is in checkmate");
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            System.out.println("Black is in checkmate");
        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            System.out.println("White is in check");
            System.out.println(game.getTeamTurn() +"'s turn");
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            System.out.println("Black is in check");
            System.out.println(game.getTeamTurn() +"'s turn");
        }

    }



    static void printLetterHeader(PrintStream out, boolean reverse) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String header = "  a  b  c  d  e  f  g  h  ";
        if (reverse) {
            out.print(new StringBuilder(header).reverse().toString());
        } else {
            out.print(header);
        }
        out.println();
    }
    static void printGame(PrintStream out, boolean reverse, ChessGame game, ChessPosition position) {
        int yStart = 8;
        int yDirection = -1;
        if (reverse) {
            yStart = 1;
            yDirection = 1;
        }
        /* if we have a position, create a non-empty positions set (or at least, non-empty as long as there are valid
         * moves...) Otherwise, we just pass an empty hashset to printLine() and that's fine.
         *
         */
        Set<ChessPosition> positions = new HashSet<>();
        if (position != null) {
            Set<ChessMove> moves = (Set<ChessMove>) game.validMoves(position);
            for (ChessMove move : moves) {
                positions.add(move.getEndPosition());
            }
        }


        for (int y = 0; y < 8; y ++) {
            int pieceCol = yStart + yDirection * y;
            out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + pieceCol);
            printLine(out, pieceCol, reverse, (y % 2) == 0, game, positions);
            out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + pieceCol);
        }
    }

    static void printLine(PrintStream out, int lineNo, boolean reverse, boolean startsWhite, ChessGame game,
                          Set<ChessPosition> validMoves) {
        int xStart = 8;
        int xDirection = -1;
        if (!reverse) {
            xStart = 1;
            xDirection = 1;
        }
        for (int x = 0; x < 8; x ++) {
            for (int subposition = 0; subposition < 3; subposition++) {
                int colorColumn = x + (startsWhite ? 1 : 0);
                int pieceCol = xStart + xDirection * x;
                if (colorColumn % 2 == 0) {
                    if (validMoves.contains(new ChessPosition(lineNo, pieceCol))) {
                        out.print(SET_BG_COLOR_DARK_GREEN);
                    } else {
                        out.print(SET_BG_COLOR_BLACK);
                    }
                } else {
                    if (validMoves.contains(new ChessPosition(lineNo, pieceCol))) {
                        out.print(SET_BG_COLOR_GREEN);
                    } else {
                        out.print(SET_BG_COLOR_WHITE);
                    }
                }

                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(lineNo, pieceCol));
                if (piece == null) {
                    out.print(" ");
                    continue;
                }
                if (piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    out.print(SET_TEXT_COLOR_RED);
                } else {
                    out.print(SET_TEXT_COLOR_BLUE);
                }
                if (subposition == 1) {
                    out.print(pieceToStringMap.get(piece.getPieceType()));
                } else {
                    out.print(" ");
                }
            }
        }

    }

}
