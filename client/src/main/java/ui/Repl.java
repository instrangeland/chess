package ui;

import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;
import websocketfacade.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                if (result != null) {
                    System.out.print(result);
                } else {
                    System.out.println();
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            if (result == null) {
                result = "";
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLUE + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notify(NotificationMessage notification) {
        System.out.print(SET_TEXT_COLOR_GREEN + notification.getMessage());
        printPrompt();
    }

    @Override
    public void loadGame(LoadMessage message) {
        System.out.println();
        GameData game = message.getGame();
        BoardUI.Perspective perspective = client.getCurrentPerspective();
        BoardUI.drawBoard(game.game(), perspective, null);
        printPrompt();
    }

    @Override
    public void error(ErrorMessage message) {
        System.out.print(SET_TEXT_COLOR_RED + message.getErr());
        printPrompt();

    }
}