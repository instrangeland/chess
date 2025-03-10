package dataaccess.SQL;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import error.ResponseError;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameSQL implements GameDAO {

    public GameSQL() {
        DataAccess.configure();
        try {
            var conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    static List<GameData> gameDataArrayList = new ArrayList<>();
    static int nextGameNum = 0;
    @Override
    public int createGame(String gameName) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public GameData getGame(int gameID) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void updateGameData(int gameID, GameData gameData) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<GameData> listGames() {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void clear() {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

}
