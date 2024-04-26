package uk.ac.soton.comp2211.users;

import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OperationLogging {
    private static final String databaseName = "user_info";

    private Connection connect() {
        return Database.getConnection(databaseName);
    }

    private void logAction(String action) {
        String query = "INSERT INTO operations(username, date, action) VALUES (?, unixepoch(), ?)";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, App.getUser().getUsername());
            prep.setString(3, action);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
