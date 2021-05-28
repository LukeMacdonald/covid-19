package app;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCConnection {

    
   
    private static final String DATABASE = "jdbc:sqlite:database/Covid.db";

    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }
    public ArrayList<String> getCountries() {
        ArrayList<String> countries = new ArrayList<String>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT * FROM Country"; 
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String countryName = results.getString("cname");
                countries.add(countryName);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return countries;
    }
    public ArrayList<Integer> getSumCases(String name, String startDate,String endDate) {
        ArrayList<Integer> cases = new ArrayList<Integer>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(Nocases) FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                int noCases = results.getInt("SUM(Nocases)");
                cases.add(noCases);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try { 
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return cases;
    }
    public ArrayList<Integer> getCases(String name, String startDate,String endDate) {
        ArrayList<Integer> cases = new ArrayList<Integer>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Nocases FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                int noCases = results.getInt("nocases");
                cases.add(noCases);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try { 
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return cases;
    }
    public ArrayList<String> getDate(String name, String startDate,String endDate) {
        ArrayList<String> dates = new ArrayList<String>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT * FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String date = results.getString("date");
                dates.add(date);
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try { 
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return dates;
    }
}