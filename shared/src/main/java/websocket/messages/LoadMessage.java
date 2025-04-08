package websocket.messages;

import model.GameData;

public class LoadMessage extends ServerMessage {


    public GameData getData() {
        return data;
    }

    private final GameData data;


    public LoadMessage(GameData gameData) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.data = gameData;
    }
}