package uk.ac.soton.comp2211.data.calculations;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import uk.ac.soton.comp2211.data.Database;
public class CampaignDataRetriever {

    public static void numberOfImpressions(Connection c) {

        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS imp_total FROM impression_log;");
            int imp_total = resultSet.getInt("imp_total") + 1;
            System.out.println("Number of Impressions: " + imp_total);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void numberOfClicks(Connection c) {
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS click_total FROM click_log;");
            int click_total = resultSet.getInt("click_total") + 1;

            // Output the row count
            System.out.println("Number of clicks: " + click_total);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void numberOfUniques(Connection c) {
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (DISTINCT ID) AS unique_count FROM click_log;");
            int unique_total = resultSet.getInt("unique_count");

            // Output the row count
            System.out.println("Number of uniques: " + unique_total);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void visitBounce(Connection c, int minVisits)  {

        String query = "SELECT COUNT(pages_viewed) AS page_bounce FROM server_log WHERE pages_viewed <= " + minVisits;
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int page_bounce = resultSet.getInt("page_bounce");

            // Output the row count
            System.out.println("Number of bounces: " + page_bounce);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public static void timeBounce(Connection c, int time_diff)  {

        String query = "SELECT COUNT(*) AS time_bounce FROM server_log WHERE (strftime('%s', exit_date) - strftime('%s', entry_date)) / 60 <= " + time_diff;
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int time_bounce = resultSet.getInt("time_bounce");

            // Output the row count
            System.out.println("Number of bounces: " + time_bounce);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public static void numberOfConversions(Connection c) {
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS conversion_count FROM server_log WHERE conversion = 'Yes';");
            int conversion_total = resultSet.getInt("conversion_count");

            // Output the row count
            System.out.println("Number of conversions: " + conversion_total);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public static void totalCost(Connection c) {
        try {
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT SUM(click_cost) AS total_cost FROM click_log");
            float total_cost = resultSet.getFloat("total_cost");

            // Output the row count
            System.out.println("total cost: " + total_cost);

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }



}








