import chess.*;
import ui.Repl;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {

        URI uri = new URI("http://localhost:8080");
        if (args.length == 1) {
            uri = new URI(args[0]);
        }
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        new Repl(uri.toString()).run();

    }
}