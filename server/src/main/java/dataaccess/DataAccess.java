package dataaccess;

import error.ResponseError;

import java.sql.SQLException;

public class DataAccess {
    private static final String[] createStatements = {
            """
                CREATE TABLE auth_table (
                    TOKEN varchar(255) NOT NULL PRIMARY KEY,
                    NAME varchar(255) NOT NULL
                );
            """,
    };
    public static void configure() throws ResponseError {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                for (String statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseError(e.getMessage(), 500);
        }
    }
}
