package ui;

import ui.Repl;
import server.ServerFascade;
import ui.EscapeSequences.*;

import static ui.EscapeSequences.*;

public class Client {
    private static State userState = State.SIGNEDOUT;
    private final ServerFascade server;
    private final String serverURL;
    private final Repl repl;
    public Client(String serverURL, Repl repl) {
        server = new ServerFascade(serverURL);
        this.serverURL = serverURL;
        this.repl = repl;
    }

    private String createHelpLine(String firstHalf, String secondHalf) {
        return SET_TEXT_COLOR_BLUE + firstHalf + " - " + SET_TEXT_COLOR_MAGENTA + secondHalf + "\n";
    }

    public String help() {
        String builtString = createHelpLine("register <USERNAME> <PASSWSORD> <EMAIL>", "create an account");
        builtString = builtString + createHelpLine("login <USERNAME> <PASSWORD>", "to play chess or observe a game");
        builtString = builtString + createHelpLine("quit", "leave program");
        builtString = builtString + createHelpLine("help", "show this list again");
        return builtString;
    }
}
