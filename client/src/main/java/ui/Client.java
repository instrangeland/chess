package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.GameID;
import model.UserData;
import request.JoinGameRequest;
import server.ServerFascade;
import websocket.messages.NotificationMessage;
import websocketFacade.NotificationHandler;
import websocketFacade.WebSocketFacade;

import java.util.List;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class Client {
    private static State userState = State.SIGNEDOUT;
    private final ServerFascade server;
    private final String serverURL;
    private final NotificationHandler repl;
    private WebSocketFacade ws;
    private List<GameData> games = null;
    private GameData currentGame = null;

    public BoardUI.Perspective getCurrentPerspective() {
        return currentPerspective;
    }

    private BoardUI.Perspective currentPerspective = null;
    private AuthData currentAuth = null;
    public Client(String serverURL, NotificationHandler repl) {
        server = new ServerFascade(serverURL);
        this.serverURL = serverURL;
        this.repl = repl;
        ws = new WebSocketFacade(this.serverURL, repl);
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
                case "redraw":
                    val = redraw(split);
                    break;
                case "leave":
                    val = leave(split);
                    break;
                case "move":
                    val = move(split);
                    break;
                case "resign":
                    val = resign(split);
                    break;
                case "legal":
                    val = legal(split);
                    break;
                default:
                    System.out.println("Command " + operation +" is invalid. Valid commands:");
                    val = help();
            }
            return val;

        } catch (IllegalArgumentException | IllegalStateException | ResponseException ex) {
            return "Error: " + ex.getMessage();
        }

    }

    private String login(String[] values) {
        assumeUserState(State.SIGNEDOUT, "login");
        if (values.length != 3) {
            throw new IllegalArgumentException("Please use the form login <USERNAME> <PASSWORD>");
        }
        try {
            currentAuth = server.login(new UserData(values[1], values[2], null));
            userState = State.SIGNEDIN;
            return "Logged in successfully as "+currentAuth.username();
        } catch (ResponseException e) {
            userState = State.SIGNEDOUT;
            return "Username or password is invalid";
        }
    }

    private String quit() {
        return "quit";
    }

    private String register(String[] values) {
        assumeUserState(State.SIGNEDOUT, "register a user");
        if (values.length != 4) {
            throw new IllegalArgumentException("Please use the form register <USERNAME> <PASSWORD> <EMAIL>");
        }
        try {
            server.register(new UserData(values[1], values[2], values[3]));
            server.logout();
            currentAuth = server.login(new UserData(values[1], values[2], null));
            userState = State.SIGNEDIN;
            return "Welcome, you are now logged in as "+currentAuth.username();
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to register, try again.");
        }
    }
    private String logout() {
        assumeUserState(State.SIGNEDIN, "logout");
        try {
            server.logout();
            userState = State.SIGNEDOUT;
            return "You have successfully logged out.";
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to log out, try again later.");
        }
    }
    private String create(String[] values) {
        assumeUserState(State.SIGNEDIN, "create a game");
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
        assumeUserState(State.SIGNEDIN, "join a game");
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
                this.currentPerspective = perspective;
                if (currentAuth.username().equals(game.whiteUsername())) {
                    System.out.println("rejoining, no need to add");
                }
                else if (game.whiteUsername() == null) {
                    server.joinGame(new JoinGameRequest("WHITE", game.gameID()));
                } else {
                    throw new IllegalArgumentException("The selected team is unavailable");
                }
            } else if (Objects.equals(values[2], "black")) {
                color = ChessGame.TeamColor.BLACK;
                perspective = BoardUI.Perspective.BLACK;
                this.currentPerspective = perspective;
                if (currentAuth.username().equals(game.blackUsername())) {
                    System.out.println("rejoining, no need to add");
                }
                else if (game.blackUsername() == null) {
                    server.joinGame(new JoinGameRequest("BLACK", game.gameID()));
                } else {
                    throw new IllegalArgumentException("The selected team is unavailable");
                }
            } else {
                throw new IllegalArgumentException("Make sure your team color is white or black");
            }
            currentGame = games.get(num-1);
            //  BoardUI.drawBoard(currentGame.game(), perspective, null);
            ws.join(currentAuth.authToken(), game.gameID(), color);
            userState = State.INGAME;
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
        if (currentGame != null) {
            for (GameData game: games) {
                if (game.gameID() == currentGame.gameID()) {
                    currentGame = game;
                }
            }
        }
    }

    private String list() {
        assumeUserState(State.SIGNEDIN, "move a piece");
        try {
            refreshGames();
            StringBuilder lines = new StringBuilder();
            //Number Name Black White
            lines.append(String.format("| %15s | %15s | %15s | %15s |\n", "Number", "Name", "White User", "Black User"));
            int gameNum = 0;
            for (GameData game:games) {
                String whiteUser = game.whiteUsername() == null ? "none" : game.whiteUsername();
                String blackUser = game.blackUsername() == null ? "none" : game.blackUsername();
                lines.append(String.format("| %15d | %15s | %15s | %15s |\n", gameNum + 1, game.gameName(),
                        whiteUser, blackUser));
                gameNum++;
            }
            return lines.toString();
        } catch (ResponseException e) {
            throw new IllegalArgumentException("Failed to retrieve games, try again later...");
        }
    }

    private String observe(String[] values) {
        assumeUserState(State.SIGNEDIN, "observe a game");
        if (values.length != 2) {
            throw new IllegalArgumentException("Please use the form observe <NUMBER>");
        }
        try {
            refreshGames();
            int num = Integer.parseInt(values[1])-1;
            GameData game = games.get(num);
            BoardUI.Perspective perspective = BoardUI.Perspective.OBSERVER;
            this.currentPerspective = perspective;
            //BoardUI.drawBoard(games.get(num).game(), perspective, null);
            ws.observe(currentAuth.authToken(), games.get(num).gameID());
            userState = State.INGAME;
            this.currentGame = game;
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
            commands = new String[]{"redraw", "leave", "make move <startpos> <endpos>", "resign", "legal <pos>"
                    , "help"};
            descriptions = new String[]{"redraws the chessboard", "leaves the current game", "makes a move",
                    "resign", "highlights legal moves for a piece at a position", "show this list again"};
        }
        for (int i=0; i<commands.length; i++) {
            builtString.append(createHelpLine(commands[i], descriptions[i]));
        }
        return builtString.toString();
    }

    private String redraw(String[] values) throws ResponseException {
        assumeUserState(State.INGAME, "redraw the board");
        if (currentPerspective == null) {
            throw new IllegalStateException("You must be observing or in a game to redraw");
        }
        refreshGames();
        BoardUI.drawBoard(currentGame.game(), currentPerspective, null);
        return "Redrawn game";

    }
    private String leave(String[] values) throws ResponseException {
        assumeUserState(State.INGAME, "leave a game");
        currentPerspective = null;
        userState = State.SIGNEDIN;
        ws.leave(currentAuth.authToken(), currentGame.gameID());
        return "You have left the game";
    }
    private String move(String[] values) throws IllegalArgumentException, ResponseException {
        assumeUserState(State.INGAME, "move a piece");
        if (values.length != 3) {
            throw new IllegalArgumentException("Please use the form move <POS> <POS>");
        }
        ChessPosition startPos = ChessPosition.fromString(values[1]);
        ChessPosition endPos = ChessPosition.fromString(values[2]);
        ChessMove move = new ChessMove(startPos, endPos);
        ws.move(currentAuth.authToken(), currentGame.gameID(), move);
        return null;
    }
    private String resign(String[] values) throws ResponseException {
        assumeUserState(State.INGAME, "resign a game");
        ws.resign(currentAuth.authToken(), currentGame.gameID());
        userState = State.SIGNEDIN;
        return "You have quit";
    }
    private String legal(String[] values) throws ResponseException {
        assumeUserState(State.INGAME, "display legal moves");
        if (values.length != 2) {
            throw new IllegalArgumentException("Please use the form legal <POS>");
        }
        ChessPosition pos = ChessPosition.fromString(values[1]);
        refreshGames();
        BoardUI.drawBoard(currentGame.game(), currentPerspective, pos);
        return "Legal moves highlighted in green";
    }

    private void assumeUserState(State state, String action) {
        if (userState != state) {
            if (state == State.INGAME) {
                throw new IllegalStateException("You must be in a game to "+action + ".");
            } else if (state == State.SIGNEDIN) {
                throw new IllegalStateException("You must be logged in and not in a game to "+action + ".");
            } else {
                throw new IllegalStateException("You must not be logged in and not in a game to "+action + ".");
            }
        }
    }
}
