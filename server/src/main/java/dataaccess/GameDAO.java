package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public interface GameDAO
{
    //returns gameID
    int createGame(String gameName);
    GameData getGame(int gameID);
    void updateGameData(int gameID, GameData gameData);
    List<GameData> listGames();

}
