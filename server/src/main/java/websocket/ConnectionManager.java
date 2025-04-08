package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connectionsByGameID =
            new ConcurrentHashMap<>();

    public void add(String username, int gameID, Session session) {
        ConcurrentHashMap<String, Connection> connectionsByUsername = connectionsByGameID.get(gameID);
        if (connectionsByUsername == null) {
            connectionsByUsername = new ConcurrentHashMap<>();
        }
        Connection connection = new Connection(username, session);
        connectionsByUsername.put(username, connection);
    }

    public void remove(String username, int gameID) {
        ConcurrentHashMap<String, Connection> connectionsByUsername = connectionsByGameID.get(gameID);
        if (connectionsByUsername == null) {
            return;
        }
        connectionsByUsername.remove(username);
    }

    public void broadcast(String excludeUsername, int gameID, ServerMessage message) throws IOException {
        ConcurrentHashMap<String, Connection> connectionsByUsername = connectionsByGameID.get(gameID);
        if (connectionsByUsername == null) {
            return;
        }

        List<Connection> removeList = new ArrayList<Connection>();
        for (var keyPair : connectionsByUsername.entrySet()) {
            Connection connection = keyPair.getValue();
            Session session = connection.session;
            String username = keyPair.getKey();

            if (session.isOpen()) {
                if (!username.equals(excludeUsername)) {
                    connection.send(message.toString());
                } else {
                    removeList.add(connection);
                }
            }
        }

        // Clean up any connections that were left open.
        for (Connection c : removeList) {
            connectionsByUsername.remove(c.username);
        }
    }
}