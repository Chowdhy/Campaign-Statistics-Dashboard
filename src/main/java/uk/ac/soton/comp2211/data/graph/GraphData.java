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

    public Pair<ArrayList<String>, ArrayList<Integer>> getData(String startMonth, String startDay, String endMonth, String endDay){
        String sql = "SELECT date FROM impression_log";
        int month = Integer.parseInt(startMonth);
        int day = Integer.parseInt(startDay);
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
                    if (count > 1) {
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
                    if (rs.getString("date").contains("2015-" + endMonth + "-" + endDay)) {
                        return new Pair<>(dates, impressions);
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

    public Pair<ArrayList<String>, ArrayList<Integer>> filterDate(String startDate, String endDate) {
        String[] startSplit = startDate.split("-");
        String[] endSplit = endDate.split("-");

        String startMonth = startSplit[1];
        String startDay = startSplit[2];
        String endMonth = endSplit[1];
        String endDay = endSplit[2];

        int newEndDay = Integer.parseInt(endDay);
        String finalEndDay;
        String finalEndMonth;
        if (newEndDay < 9) {
            newEndDay = newEndDay + 1;
            finalEndDay = "0" + newEndDay;
            finalEndMonth = endMonth;
        } else if (newEndDay < 31) {
            newEndDay = newEndDay + 1;
            finalEndDay = "" + newEndDay;
            finalEndMonth = endMonth;
        } else {
            finalEndDay = "01";
            finalEndMonth = "02";
        }

        return getData(startMonth, startDay, finalEndMonth, finalEndDay);
    }
}
