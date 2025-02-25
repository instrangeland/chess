package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class AuthRam implements AuthDAO{
    HashMap<String, AuthData> authDataHashMap = new HashMap<>();
    public void clear() {
        authDataHashMap.clear();
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, username);
        authDataHashMap.put(authToken, data);
        return data;
    }

    public AuthData getAuth(String authToken) {
        return authDataHashMap.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authDataHashMap.put(authToken, null);
    }
}
