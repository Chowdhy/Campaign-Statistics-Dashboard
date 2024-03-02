package uk.ac.soton.comp2211.data.graph;

import javafx.util.Pair;

import java.util.ArrayList;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GraphData {

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<Integer> impressions = new ArrayList<>();

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:data/campaign.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Pair<ArrayList<String>, ArrayList<Integer>> selectAll(){
        String sql = "SELECT date FROM impression_log";
        int month = 1;
        int day = 1;
        int count = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String formattedDay = (day < 10 ? "0" : "") + day;
                if (rs.getString("date").contains("2015-0" + month + "-" + formattedDay)) {
                    count += 1;
                } else {
                    dates.add("2015-0" + month + "-" + formattedDay);
                    impressions.add(count);
                    count = 1;
                    if (day != 31) {
                        day += 1;
                    } else {
                        month = 2;
                        day = 1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        dates.add("2015-0" + month + "-" + day);
        impressions.add(count);

        return new Pair<>(dates, impressions);
    }
}
