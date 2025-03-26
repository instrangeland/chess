package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;

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

    static void drawBoard(ChessGame game, Perspective perspective) {
        ChessBoard board = game.getBoard();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        boolean reverse = (perspective == Perspective.BLACK);
        printLetterHeader(out, reverse);
        printGame(out, reverse, board);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_BG_COLOR);
    }

    static void printLetterHeader(PrintStream out, boolean reverse) {
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        String header = " abcdefgh ";
        if (reverse) {
            out.print(new StringBuilder(header).reverse().toString());
        } else {
            out.print(header);
        }
        out.println();
    }
    static void printGame(PrintStream out, boolean reverse, ChessBoard board) {
        int yStart = 8;
        int yDirection = -1;
        if (reverse) {
            yStart = 1;
            yDirection = 1;
        }
        for (int y = 0; y < 8; y ++) {
            int pieceCol = yStart + yDirection * y;
            out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + pieceCol);
            printLine(out, pieceCol, reverse, (y % 2) == 0, board);
            out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + pieceCol);

        }
    }

    static void printLine(PrintStream out, int lineNo, boolean reverse, boolean startsWhite, ChessBoard board) {
        int xStart = 8;
        int xDirection = -1;
        if (!reverse) {
            xStart = 1;
            xDirection = 1;
        }
        for (int x = 0; x < 8; x ++) {
            int colorColumn = x + (startsWhite ? 1 : 0);
            if (colorColumn % 2 == 0) {
                out.print(SET_BG_COLOR_BLACK);
            } else {
                out.print(SET_BG_COLOR_WHITE);
            }
            int piece_col = xStart + xDirection * x;
            ChessPiece piece = board.getPiece(new ChessPosition(lineNo, piece_col));
            if (piece == null) {
                out.print(" ");
            } else {
                if (piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    out.print(SET_TEXT_COLOR_RED);
                } else {
                    out.print(SET_TEXT_COLOR_BLUE);
                }
                out.print(pieceToStringMap.get(piece.getPieceType()));
            }
        }

    }

}
