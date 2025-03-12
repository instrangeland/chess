package dataaccess.SQL;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import error.ResponseError;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserSQL implements UserDAO {
    private final Connection connection;
    public UserSQL() {
        DataAccess.configure();
        try {
            connection = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }
    public UserData getUser(String username) {
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT PASSWORD_HASH, EMAIL FROM user_table WHERE TOKEN=?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                String email;
                String passwordHash;
                if (resultSet.next()) {
                    email = resultSet.getString("EMAIL");
                    passwordHash = resultSet.getString("PASSWORD_HASH");
                } else {
                    return null;
                }
                return new UserData(username, passwordHash, email);
            }
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    @Override
    public UserData createUser(String username, String password, String email) {
        try (PreparedStatement statement = connection.prepareStatement(
                             "INSERT INTO user_table (USERNAME, PASSWORD_HASH, EMAIL) VALUES (?,?,?)")) {
            String hashedPWD = BCrypt.hashpw(password, BCrypt.gensalt());
            statement.setString(1, username);
            statement.setString(2, hashedPWD);
            statement.setString(3, email);
            statement.executeUpdate();
            return new UserData(username, hashedPWD, email);
        } catch (SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }

    }

    public void clear() {
        DataAccess.runSimpleCommand("TRUNCATE TABLE user_table");
    }
}
