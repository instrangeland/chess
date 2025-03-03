package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameRam implements GameDAO{
    static List<GameData> gameDataArrayList = new ArrayList<>();
    static int nextGameNum = 0;
    @Override
    public int createGame(String gameName) {
        nextGameNum++;
        ChessGame game = new ChessGame();
        gameDataArrayList.add(new GameData(nextGameNum, null, null, gameName, game));
        return nextGameNum;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataArrayList.get(gameID-1);
    }

    @Override
    public void updateGameData(int gameID, GameData gameData) {
        gameDataArrayList.set(gameID-1, gameData);
    }

    @Override
    public List<GameData> listGames() {
        return gameDataArrayList;
    }

    public void clear() {
        System.out.println("clearing games");
        gameDataArrayList.clear();
        nextGameNum = 0;
    }

}
