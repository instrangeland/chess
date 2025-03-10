package dataaccess.SQL;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import error.ResponseError;
import model.AuthData;

import java.sql.*;

public class AuthSQL implements AuthDAO {

    public AuthSQL() {
        DataAccess.configure();
        try {
            var conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    public void clear() {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public AuthData createAuth(String username) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Not implemented yet");
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
        var statement = "DELETE FROM auth_table WHERE TOKEN=?";
        try (var conn = DatabaseManager.getConnection()) {
            conn.prepareStatement(statement);
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }
}
