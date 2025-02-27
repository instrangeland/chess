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
        ChessGame game = new ChessGame();
        gameDataArrayList.set(nextGameNum, new GameData(nextGameNum, null, null, gameName, game));
        nextGameNum++;
        return nextGameNum-1;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameDataArrayList.get(gameID);
    }

    @Override
    public void updateGameData(int gameID, GameData gameData) {
        gameDataArrayList.set(gameID, gameData);
    }

    @Override
    public List<GameData> listGames() {
        return gameDataArrayList;
    }

}
