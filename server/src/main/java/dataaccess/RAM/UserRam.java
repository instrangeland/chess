package dataaccess.RAM;

import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class UserRam implements UserDAO {
    static HashMap<String, UserData> userDataHashMap = new HashMap<>();
    public UserData getUser(String username) {
        return userDataHashMap.get(username);
    }

    @Override
    public UserData createUser(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);
        createUser(userData);
        return userData;
    }

    @Override
    public void createUser(UserData userData) {
        userDataHashMap.put(userData.username(), userData);
    }

    public void clear() {
        userDataHashMap.clear();
    }
}
