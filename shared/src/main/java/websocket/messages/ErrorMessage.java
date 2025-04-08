package websocket.messages;

public class ErrorMessage extends ServerMessage{
    public String getErr() {
        return err;
    }

    private final String err;


    public ErrorMessage(String err) {
        super(ServerMessageType.ERROR);
        this.err = err;
    }
}
