package dataaccess;

import model.AuthData;
import java.util.HashMap;
import java.util.UUID;

public class AuthRam implements AuthDAO{
    static HashMap<String, AuthData> authDataHashMap = new HashMap<>();
    public void clear() {
        authDataHashMap.clear();
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, username);
        authDataHashMap.put(authToken, data);
        printAuth();
        return data;
    }

    public void printAuth() {
        System.out.println(authDataHashMap.toString());
    }

    public AuthData getAuth(String authToken) {
        printAuth();
        System.out.println(authToken);
        System.out.println(authDataHashMap.get(authToken));
        return authDataHashMap.get(authToken);
    }

    public void deleteAuth(String authToken) {
        authDataHashMap.put(authToken, null);
        System.out.println("deleted an auth");
        printAuth();
    }
}
