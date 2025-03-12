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
        userDataHashMap.put(userData.username(), userData);
        return userData;
    }


    public void clear() {
        userDataHashMap.clear();
    }
}
