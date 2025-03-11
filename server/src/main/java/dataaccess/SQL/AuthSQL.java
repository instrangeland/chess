package dataaccess.SQL;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import error.ResponseError;
import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class AuthSQL implements AuthDAO {
    private final Connection connection;
    public AuthSQL() {
        DataAccess.configure();
        try {
            connection = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    public void clear() {
        DataAccess.runSimpleCommand("TRUNCATE TABLE auth_table");
    }

    public AuthData createAuth(String username) {
        // TODO: Implement this method
        String authToken = UUID.randomUUID().toString();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO auth_table (TOKEN, KEY) VALUES (?,?)")) {
            statement.setString(1, authToken);
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }


        return new AuthData(authToken, username);
    }

    public void printAuth() {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public AuthData getAuth(String authToken) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteAuth(String authToken) throws DataAccessException, SQLException {
        DataAccess.runSimpleCommand("DELETE FROM auth_table WHERE TOKEN=?");
    }
}
