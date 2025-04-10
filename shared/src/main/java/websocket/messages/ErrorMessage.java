package websocket.messages;

public class ErrorMessage extends ServerMessage{
    public String getErr() {
        return errorMessage;
    }

    private final String errorMessage;


    public ErrorMessage(String err) {
        super(ServerMessageType.ERROR);
        this.errorMessage = err;
    }
}
