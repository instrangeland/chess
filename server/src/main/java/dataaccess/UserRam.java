package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;

public class UserRam implements UserDAO{
    static HashMap<String, UserData> authDataHashMap = new HashMap<>();
    public UserData getUser(String username) {
        return authDataHashMap.get(username);
    }

    @Override
    public UserData createUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        createUser(userData);
        return userData;
    }

    @Override
    public void createUser(UserData userData) {
        authDataHashMap.put(userData.username(), userData);
    }

}
