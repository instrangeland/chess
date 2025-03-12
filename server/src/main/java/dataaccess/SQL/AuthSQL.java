package dataaccess.SQL;

import dataaccess.AuthDAO;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import error.ResponseError;
import error.UnauthorizedError;
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
        String authToken = UUID.randomUUID().toString();
        try (PreparedStatement statement =
                     connection.prepareStatement("INSERT INTO auth_table (TOKEN, USERNAME) VALUES (?,?)")) {
            statement.setString(1, authToken);
            statement.setString(2, username);
            statement.executeUpdate();
            return new AuthData(authToken, username);
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }


    public AuthData getAuth(String authToken) {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT USERNAME FROM auth_table WHERE TOKEN=?")) {
            statement.setString(1, authToken);
            try (ResultSet resultSet = statement.executeQuery()) {
                String username;
                if (resultSet.next()) {
                    username = resultSet.getString("USERNAME");
                } else {
                    return null;
                }
                return new AuthData(authToken, username);
            }
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException, SQLException {
        if (authToken == null)
            throw new ResponseError("Cannot pass deleteAuth null", 500);
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM auth_table WHERE TOKEN=?")) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }
}
