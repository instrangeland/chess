package websocket.messages;

public class NotificationMessage extends ServerMessage {

    public String getMessage() {
        return message;
    }

    private final String message;


    public NotificationMessage(String msg) {
        super(ServerMessageType.NOTIFICATION);
        this.message = msg;
    }
}
