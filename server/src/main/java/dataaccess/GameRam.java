package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public class GameRam implements GameDAO{
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGameData(int gameID, GameData gameData) {

    }

    @Override
    public List<ChessGame> listGames() {
        return List.of();
    }
}
