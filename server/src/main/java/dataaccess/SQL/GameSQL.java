package dataaccess.SQL;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import error.ResponseError;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameSQL implements GameDAO {

    private final Connection connection;
    public GameSQL() {
        DataAccess.configure();
        try {
            connection = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    static List<GameData> gameDataArrayList = new ArrayList<>();
    static int nextGameNum = 1;
    @Override
    public int createGame(String gameName) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO game_table (ID, WHITE_USERNAME, BLACK_USERNAME, GAME_NAME, JSON) VALUES (?,?,?,?,?)"))
        {
            statement.setInt(1, nextGameNum);
            statement.setString(2, null);
            statement.setString(3, null);
            statement.setString(4, gameName);
            ChessGame game = new ChessGame();
            String chessPickled = new Gson().toJson(game);
            statement.setString(5, chessPickled);
            statement.executeUpdate();
            nextGameNum++;
            return nextGameNum - 1;
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "SELECT * FROM game_table WHERE ID=?")) {
            statement.setInt(1, gameID);
            try (ResultSet resultSet = statement.executeQuery()) {
                String whiteUsername;
                String blackUsername;
                String gameName;
                ChessGame game;
                if (resultSet.next()) {
                    whiteUsername = resultSet.getString("WHITE_USERNAME");
                    blackUsername = resultSet.getString("BLACK_USERNAME");
                    gameName = resultSet.getString("GAME_NAME");
                    game = new Gson().fromJson(resultSet.getString("JSON"), ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);

                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    @Override
    public void updateGameData(int gameID, GameData gameData) {
        try (PreparedStatement statement =
                     connection.prepareStatement(
                             "UPDATE game_table SET WHITE_USERNAME=?, BLACK_USERNAME=?," +
                                     " GAME_NAME=?, JSON=? WHERE ID=?")) {
            statement.setString(1, gameData.whiteUsername());
            statement.setString(2, gameData.blackUsername());
            statement.setString(3, gameData.gameName());
            statement.setString(4, new Gson().toJson(gameData.game()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    @Override
    public List<GameData> listGames() {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT * FROM game_table")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                String whiteUsername;
                String blackUsername;
                String gameName;
                int gameNumber;
                ChessGame game;
                List<GameData> gameData = new ArrayList<>();
                if (resultSet.next()) {
                    whiteUsername = resultSet.getString("WHITE_USERNAME");
                    blackUsername = resultSet.getString("BLACK_USERNAME");
                    gameName = resultSet.getString("GAME_NAME");
                    gameNumber = resultSet.getInt("ID");
                    game = new Gson().fromJson(resultSet.getString("JSON"), ChessGame.class);
                    gameData.add(new GameData(gameNumber, whiteUsername, blackUsername, gameName, game));
                } else {
                    return gameData;
                }
            }
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
        return new ArrayList<>();
    }

    public void clear() {
        DataAccess.runSimpleCommand("TRUNCATE TABLE game_table");
    }

}
