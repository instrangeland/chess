package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface UserDAO {
    public abstract UserData getUser(String username);
    public abstract UserData createUser(String username, String password, String email);
    public abstract void createUser(UserData userData);
    public void clear();
}
