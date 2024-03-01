package uk.ac.soton.comp2211.data.calculations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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




}


