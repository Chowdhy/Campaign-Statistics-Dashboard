package uk.ac.soton.comp2211.data.calculations;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import uk.ac.soton.comp2211.data.Database;
public class CampaignDataRetriever {
public Connection connection;

public Statement statement;

private int numberOfImpressions;

private int numberOfClicks;

private int numberOfUniques;

private int numberOfBouncesVisit;

private int numberOfBouncesTime;

private int totalConversions;

private float totalCost;

private int numberOfPages;

private int timeDifference;


    public CampaignDataRetriever(Connection c, int pageBounce, int timeDiff)  {
        try {
            this.connection = c;
            this.statement = c.createStatement();
            this.numberOfPages = pageBounce;
            this.timeDifference = timeDiff;
            numberOfImpressions();
            numberOfClicks();
            numberOfUniques();
            visitBounce(numberOfBouncesVisit);
            timeBounce(numberOfBouncesTime);
            numberOfConversions();
            totalCost();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public CampaignDataRetriever(Connection c)  {
        try {
            this.connection = c;
            this.statement = c.createStatement();
            this.numberOfPages = 1;
            this.timeDifference = 1;
            numberOfImpressions();
            numberOfClicks();
            numberOfUniques();
            visitBounce(numberOfBouncesVisit);
            timeBounce(numberOfBouncesTime);
            numberOfConversions();
            totalCost();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void numberOfImpressions() {

        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS imp_total FROM impression_log;");
            this.numberOfImpressions = resultSet.getInt("imp_total") + 1;
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void numberOfClicks() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS click_total FROM click_log;");
            this.numberOfClicks = resultSet.getInt("click_total") + 1;

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }




    public void numberOfUniques() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (DISTINCT ID) AS unique_count FROM click_log;");
            this.numberOfUniques = resultSet.getInt("unique_count");

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }



    public void visitBounce(int minVisits)  {

        String query = "SELECT COUNT(pages_viewed) AS page_bounce FROM server_log WHERE pages_viewed <= " + minVisits;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            this.numberOfBouncesVisit = resultSet.getInt("page_bounce");

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void timeBounce(int time_diff)  {

        String query = "SELECT COUNT(*) AS time_bounce FROM server_log WHERE (strftime('%s', exit_date) - strftime('%s', entry_date)) / 60 <= " + time_diff;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            this.numberOfBouncesTime = resultSet.getInt("time_bounce");


            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public  void numberOfConversions() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT (*) AS conversion_count FROM server_log WHERE conversion = 'Yes';");
            this.totalConversions = resultSet.getInt("conversion_count");
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void totalCost() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT SUM(click_cost) AS total_cost FROM click_log");
            this.totalCost = resultSet.getFloat("total_cost");

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int getNumberOfImpressions(){
        return this.numberOfImpressions;
    }

    public int getNumberOfClicks(){
        return this.numberOfClicks;
    }

    public int getNumberOfUniques(){
        return this.numberOfUniques;
    }

    public int getNumberOfBouncesVisit(){
        return numberOfBouncesVisit;
    }

    public int getNumberOfBouncesTime(){
        return this.numberOfBouncesTime;
    }
    public int getNumberOfConversions(){
        return this.totalConversions;
    }

    public float getTotalCost(){
        return this.totalCost;
    }


}








