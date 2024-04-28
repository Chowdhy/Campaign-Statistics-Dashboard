package uk.ac.soton.comp2211.users;

import uk.ac.soton.comp2211.App;
import uk.ac.soton.comp2211.data.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationLogging {
    private static final String databaseName = "user_info";

    private static Connection connect() {
        return Database.getConnection(databaseName);
    }

    public static void logAction(String action) {
        String query = "INSERT INTO operations(username, timestamp, action) VALUES (?, unixepoch(), ?)";

        try (Connection conn = connect();
             PreparedStatement prep = conn.prepareStatement(query)) {
            prep.setString(1, App.getUser().getUsername());
            prep.setString(2, action);

            prep.execute();

            System.out.println("Logged action: " + action);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String[]> getLog() {
        if (!App.getUser().getPermissions().equals(Permissions.ADMIN)) return null;

        List<String[]> log = new ArrayList<>();

        String query = "SELECT strftime('%Y-%m-%dT%H:%M:%S', datetime(timestamp, 'unixepoch')), username, action FROM operations";

        try (Connection conn = connect();
             Statement stat = conn.createStatement();
             ResultSet rs = stat.executeQuery(query)) {

            while (rs.next()) {
                String[] entry = {rs.getString(1), rs.getString(2), rs.getString(3)};
                log.add(entry);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return log;
    }

    public static File getLogCSV(String path) {
        List<String[]> entries = getLog();

        if (entries == null) return null;

        File outputFile = new File(path);

        try {
            outputFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("Timestamp,Username,Action");
            entries.stream()
                    .map(entry -> String.join(",", entry))
                    .forEach(writer::println);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return outputFile;
    }
}
