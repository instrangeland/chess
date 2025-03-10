package dataaccess;

import error.ResponseError;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;
import com.google.gson.Gson;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class AuthSQL implements AuthDAO{
    static HashMap<String, AuthData> authDataHashMap = new HashMap<>();

    public AuthSQL() {
        DataAccess.configure();
        try {
            var conn = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    public void clear() {
        authDataHashMap.clear();
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, username);
        authDataHashMap.put(authToken, data);
        printAuth();
        return data;
    }

    public void printAuth() {
        System.out.println(authDataHashMap.toString());
    }

    public AuthData getAuth(String authToken) {
        printAuth();
        System.out.println(authToken);
        System.out.println(authDataHashMap.get(authToken));
        return authDataHashMap.get(authToken);
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
