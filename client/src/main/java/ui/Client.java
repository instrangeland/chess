package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.GameID;
import model.UserData;
import request.JoinGameRequest;
import ui.Repl;
import server.ServerFascade;
import ui.EscapeSequences.*;

import javax.naming.OperationNotSupportedException;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class Client {
    private static State userState = State.SIGNEDOUT;
    private final ServerFascade server;
    private final String serverURL;
    private final Repl repl;
    private List<GameData> games = null;
    public Client(String serverURL, Repl repl) {
        server = new ServerFascade(serverURL);
        this.serverURL = serverURL;
        this.repl = repl;
    }

    public String eval(String in) {
        String lowered = in.toLowerCase();
        String[] split = lowered.split(" ");
        String operation = split[0];
        try {
            String val;
            switch (operation) {
                case "login":
                    val = login(split);
                    break;
                case "help":
                    val = help();
                    break;
                case "quit":
                    val = quit();
                    break;
                case "register":
                    val = register(split);
                    break;
                case "logout":
                    val = logout();
                    break;
                case "create":
                    val = create(split);
                    break;
                case "list":
                    val = list();
                    break;
                case "join":
                    val = join(split);
                    break;
                case "observe":
                    val = observe(split);
                    break;
                default:
                    System.out.println("Command " + operation +" is invalid. Valid commands:");
                    val = help();
            }
            return val;

        } catch (IllegalArgumentException | IllegalStateException ex) {
            return "Error: " + ex.getMessage();
        }

    }

    private String login(String[] values) {
        if (userState != State.SIGNEDOUT) {
            System.out.println(logout());
        }
        if (values.length != 3) {
            throw new IllegalArgumentException("Please use the form login <USERNAME> <PASSWORD>");
        }
        try {
            AuthData data = server.login(new UserData(values[1], values[2], null));
            userState = State.SIGNEDIN;
            return "Logged in successfully as "+data.username();
        } catch (ResponseException e) {
            userState = State.SIGNEDOUT;
            return "Username or password is invalid";
        }
    }

    private String quit() {
        return "quit";
    }

    private String register(String[] values) {
        if (userState != State.SIGNEDOUT) {
            throw new IllegalStateException("You must be logged out to create a user.");
        }
        if (values.length != 4) {
            throw new IllegalArgumentException("Please use the form register <USERNAME> <PASSWORD> <EMAIL>");
        }
        try {
            AuthData data = server.register(new UserData(values[1], values[2], values[3]));
            userState = State.SIGNEDIN;
            return "Welcome, you are now logged in as "+data.username();
        } catch (ResponseException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Failed to register, try again.");
        }
    }
    private String logout() {
        if (userState != State.SIGNEDIN) {
            throw new IllegalStateException("You must be logged in and not in a game to logout.");
        }
        try {
            server.logout();
            userState = State.SIGNEDOUT;
            return "You have successfully logged out.";
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to log out, try again later.");
        }
    }
    private String create(String[] values) {
        if (userState != State.SIGNEDIN) {
            throw new IllegalStateException("You must be logged in and not in a game to create a game.");
        }
        if (values.length != 2) {
            throw new IllegalArgumentException("Please use the form create <GAME_NAME>");
        }
        try {
            GameID data = server.createGame(values[1]);
            return "Game has been created";
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to create, try again later...");
        }
    }
    private String join(String[] values) {
        if (userState != State.SIGNEDIN) {
            throw new IllegalStateException("You must be logged in and not in a game to join a game.");
        }
        if (values.length != 3) {
            throw new IllegalArgumentException("Please use the form join <NUMBER> [WHITE|BLACK]");
        }
        try {
            refreshGames();
            int num = Integer.parseInt(values[1]);
            GameData game = games.get(num - 1);
            BoardUI.Perspective perspective;
            ChessGame.TeamColor color;
            if (Objects.equals(values[2], "white")) {
                color = ChessGame.TeamColor.WHITE;
                perspective = BoardUI.Perspective.WHITE;
                if (game.whiteUsername() == null) {
                    server.joinGame(new JoinGameRequest("WHITE", game.gameID()));
                } else {
                    throw new IllegalArgumentException("The selected team is unavailable");
                }
            } else if (Objects.equals(values[2], "black")) {
                color = ChessGame.TeamColor.BLACK;
                perspective = BoardUI.Perspective.BLACK;
                if (game.blackUsername() == null) {
                    server.joinGame(new JoinGameRequest("BLACK", game.gameID()));
                } else {
                    throw new IllegalArgumentException("The selected team is unavailable");
                }
            } else {
                throw new IllegalArgumentException("Make sure your team color is white or black");
            }
            BoardUI.drawBoard(games.get(num).game(), perspective);
            return "Joined game " +num;
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to create, try again later...");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ensure the <NUMBER> is a valid integer!");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "Ensure the <NUMBER> is a game number that exists (try the list command)");
        }
    }

    private void refreshGames() throws ResponseException {
        games = server.listGames().games();
    }

    private String list() {
        try {
            refreshGames();
            StringBuilder lines = new StringBuilder();
            //Number Name Black White
            lines.append(String.format("| %15s | %15s | %15s | %15s |\n", "Number", "Name", "White", "Black"));
            int gameNum = 0;
            for (GameData game:games) {
                lines.append(String.format("| %15d | %15s | %15s | %15s |\n", gameNum + 1, game.gameName(),
                        game.whiteUsername(), game.blackUsername()));
                gameNum++;
            }
            return lines.toString();
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to retrieve games, try again later...");
        }
    }

    private String observe(String[] values) {
        if (userState != State.SIGNEDIN) {
            throw new IllegalStateException("You must be logged in and not in a game to observe a game.");
        }
        if (values.length != 2) {
            throw new IllegalArgumentException("Please use the form observe <NUMBER>");
        }
        try {
            refreshGames();
            int num = Integer.parseInt(values[1])-1;
            GameData game = games.get(num);
            BoardUI.Perspective perspective = BoardUI.Perspective.OBSERVER;
            BoardUI.drawBoard(games.get(num).game(), perspective);
            return "Viewing game " + (num+1);
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to create, try again later...");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ensure the <NUMBER> is a valid integer!");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                    "Ensure the <NUMBER> is a game number that exists (try the list command)");
        }
    }

    private String createHelpLine(String firstHalf, String secondHalf) {
        return SET_TEXT_COLOR_BLUE + firstHalf + " - " + SET_TEXT_COLOR_MAGENTA + secondHalf + "\n";
    }

    public String help() throws IllegalArgumentException {
        StringBuilder builtString = new StringBuilder("Commands \n");
        String[] commands;
        String[] descriptions;
        if (userState == State.SIGNEDOUT) {
            commands = new String[]{"register <USERNAME> <PASSWSORD> <EMAIL>", "login <USERNAME> <PASSWORD>",
                    "quit", "help"};
            descriptions = new String[]{"create an account", "play chess or observe a game", "leave program",
                    "show this list again"};
        } else if (userState == State.SIGNEDIN) {
            commands = new String[]{"create <NAME>", "logout", "list", "join <NUMBER> [WHITE|BLACK]", "observe <NUMBER>"
                    , "help"};
            descriptions = new String[]{"create an game with a specific name", "logs out", "lists all games",
                    "joins a numbered game", "watch a current game", "show this list again"};
        } else {
            throw new IllegalArgumentException("game commands have not been implemented yet");
        }
        for (int i=0; i<commands.length; i++) {
            builtString.append(createHelpLine(commands[i], descriptions[i]));
        }
        return builtString.toString();
    }
}
