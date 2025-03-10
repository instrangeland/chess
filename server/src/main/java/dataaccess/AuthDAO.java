package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    void clear();
    AuthData createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken) throws DataAccessException, SQLException;


}
