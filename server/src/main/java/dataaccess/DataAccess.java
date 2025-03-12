package dataaccess;

import error.ResponseError;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataAccess {
    private static final String[] CREATE_STATEMENTS = {
            """
                CREATE TABLE IF NOT EXISTS auth_table (
                    TOKEN varchar(255) PRIMARY KEY,
                    USERNAME varchar(255) NOT NULL
                );
            """, """
                CREATE TABLE IF NOT EXISTS user_table (              
                    USERNAME varchar(255) PRIMARY KEY,
                    PASSWORD_HASH varchar(255) NOT NULL,
                    EMAIL varchar(255) NOT NULL
                );
                """,
            """
                CREATE TABLE IF NOT EXISTS game_table (
                    ID INT PRIMARY KEY,
                    WHITE_USERNAME varchar(255),
                    BLACK_USERNAME varchar(255),
                    GAME_NAME varchar(255) NOT NULL,
                    JSON TEXT NOT NULL
                );
            """,
    };
    public static void configure() throws ResponseError {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                for (String statement : CREATE_STATEMENTS) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

    public static void runSimpleCommand(String statement) {
        try (var conn = DatabaseManager.getConnection()) {
            try(PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }

}
