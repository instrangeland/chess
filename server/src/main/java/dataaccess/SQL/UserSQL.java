package dataaccess.SQL;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import error.ResponseError;
import model.UserData;

import java.util.HashMap;

public class UserSQL implements UserDAO {
    public UserSQL() {
        DataAccess.configure();
        try {
            var conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }
    public UserData getUser(String username) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public UserData createUser(String username, String password, String email) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void createUser(UserData userData) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void clear() {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
