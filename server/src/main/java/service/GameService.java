package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.GameRam;

public class GameService {
    public static void setGameDAO(GameDAO gameDAO) {
        GameService.gameDAO = gameDAO;
    }

    static private GameDAO gameDAO;

    static public void deleteGames() {

    }
}
