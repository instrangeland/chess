package websocket.messages;

import model.GameData;

public class LoadMessage extends ServerMessage {


    public GameData getGame() {
        return game;
    }

    private final GameData game;


    public LoadMessage(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.game = gameData;
    }
}