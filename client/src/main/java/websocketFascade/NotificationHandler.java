package websocketFascade;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notification);
    void loadGame(LoadMessage message);
    void error(ErrorMessage message);
}