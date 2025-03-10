package service;

import dataaccess.GameDAO;
import error.BadRequestError;
import error.TakenError;
import model.GameData;
import response.ListGamesResponse;
import response.NewGameResponse;

public class GameService {
    public static void setGameDAO(GameDAO gameDAO) {
        GameService.gameDAO = gameDAO;
    }

    public static void updateGame(int gameNum, GameData newData) {
        gameDAO.updateGameData(gameNum, newData);
    }

    public static GameData getGame(int gameNum) {
        System.out.println("getting game "+gameNum);
        return gameDAO.getGame(gameNum);
    }

    static private GameDAO gameDAO;

    static public void deleteGames() {
        gameDAO.clear();
    }

    static public ListGamesResponse listGames() {
        return new ListGamesResponse(gameDAO.listGames());
    }

    static public void joinGame(String username, String teamColor, int gameNum) {
        try {
            GameData data = GameService.getGame(gameNum);
            if (data == null) {
                throw new BadRequestError();
            }
            GameData newData;
            if (teamColor.equals("BLACK")) {
                if (data.blackUsername() == null) {
                    newData = new GameData(data.gameID(), data.whiteUsername(), username, data.gameName(), data.game());
                } else {
                    throw new TakenError();
                }
            } else {
                if (data.whiteUsername() == null) {
                    newData = new GameData(data.gameID(), username, data.blackUsername(), data.gameName(), data.game());
                } else {
                    throw new TakenError();
                }
            }
            GameService.updateGame(gameNum, newData);
        } catch (IndexOutOfBoundsException e) {
            throw new BadRequestError();
        }
    }

    static public NewGameResponse newGame(String gameName) {
        NewGameResponse game = new NewGameResponse(gameDAO.createGame(gameName));
        System.out.println("made new game "+game.gameID());
        return game;
    }

}
