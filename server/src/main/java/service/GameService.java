package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.GameRam;
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

    static public NewGameResponse newGame(String gameName) {
        NewGameResponse game = new NewGameResponse(gameDAO.createGame(gameName));
        System.out.println("made new game "+game.gameID());
        return game;
    }

}
