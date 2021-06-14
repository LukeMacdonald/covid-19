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

//LUKES CLASSES    
    //Returns Array of All Countries
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
    //Returns Array of All Australian States
    public ArrayList<String> getStates() {
        ArrayList<String> states = new ArrayList<String>();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT * FROM State JOIN Country ON state.CountryID = country.CountryID WHERE cname = 'Australia';"; 
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String stateName = results.getString("sname");
                states.add(stateName);
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
        return states;
    }
    //Get Total Amount of Cases for A Country In a Specified Date
    public int getSumCases(String name, String startDate,String endDate) {
        Connection connection = null;
        int sumCases = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(Nocases) FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";

            ResultSet results = statement.executeQuery(query);
            String cases = results.getString("SUM(Nocases)");
            sumCases = Integer.parseInt(cases);
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
        return sumCases;
    }
    //Get Total Amount of Cases for A State In a Specified Date
    public int getSSumCases(String name, String startDate,String endDate) {
        Connection connection = null;
        int sumCases = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
    
            String query = "SELECT SUM(Nocases) FROM State s JOIN StateCount ON s.StateId = statecount.stateID"
                             + " WHERE s.sname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";
    
            ResultSet results = statement.executeQuery(query);
            String cases = results.getString("SUM(Nocases)");
            sumCases = Integer.parseInt(cases);
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
        return sumCases;
    }
    //Get Date of Highest Infection Increase During Specifed Date Range
    public String getHighestDate(String name, String startDate,String endDate) {
        Connection connection = null;
        String highDate = "";
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Strftime('%d/%m/%Y',date) AS 'date' FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "') ORDER BY noCases DESC LIMIT 1;";

            ResultSet results = statement.executeQuery(query);
            highDate = results.getString("date");
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
        }return highDate;
    }
    //x Days ago
    public int getXDays(String country,String days) {
        Connection connection = null;
        int sumCases = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
    
            String query = "SELECT SUM(Nocases) FROM Country c JOIN Countrycases cc ON c.CountryId = cc.countryID"
                             + " WHERE c.cname = '" + country + "' AND (date BETWEEN DATE('2021-04-22','-" + days + " day') AND '2021-04-22');";
    
            ResultSet results = statement.executeQuery(query);
            String cases = results.getString("SUM(Nocases)");
            sumCases = Integer.parseInt(cases);
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
        return sumCases;
    }
    
    public int getXDeaths(String country,String days) {
        Connection connection = null;
        int sumDeaths = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
    
            String query = "SELECT SUM(Nodeaths) FROM Country c JOIN Countrycases cc ON c.CountryId = cc.countryID"
                             + " WHERE c.cname = '" + country + "' AND (date BETWEEN DATE('2021-04-22','-" + days + " day') AND '2021-04-22');";
    
            ResultSet results = statement.executeQuery(query);
            String deaths = results.getString("SUM(Nodeaths)");
            sumDeaths = Integer.parseInt(deaths);
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
        return sumDeaths;
    }
    
    public int getSumDeaths(String name, String startDate,String endDate) {
        Connection connection = null;
        int sumCases = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(Nodeaths) FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "')";

            ResultSet results = statement.executeQuery(query);
            String cases = results.getString("SUM(Nodeaths)");
            sumCases = Integer.parseInt(cases);
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
        return sumCases;
    }
    
    public int population(String name) {
        Connection connection = null;
        int pop = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT population FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name +"';";
            ResultSet results = statement.executeQuery(query);
            String population = results.getString("population");
            pop = Integer.parseInt(population);
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
        return pop;
    }
    
    public int statePopulation(String name) {
        Connection connection = null;
        int pop = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT population FROM State s WHERE s.sname = '" + name +"';";
            ResultSet results = statement.executeQuery(query);
            String population = results.getString("population");
            pop = Integer.parseInt(population);
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
        return pop;
    }
    
    public String getSHighestDate(String name, String startDate,String endDate) {
        Connection connection = null;
        String highDate = "";
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Strftime('%d/%m/%Y',date) AS 'date' FROM State s JOIN Statecount ON s.stateid = Statecount.stateID"
                             + " WHERE s.sname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "') ORDER BY noCases DESC LIMIT 1;";

            ResultSet results = statement.executeQuery(query);
            highDate = results.getString("date");
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
        }return highDate;
    }
    
    public ArrayList<String> getDate(String name, String startDate,String endDate) {
        Connection connection = null;
        ArrayList<String>date = new ArrayList<String>();
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Strftime('%d/%m/%Y',date) AS 'date' FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "'); ORDER BY date";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String dates = results.getString("date");
                date.add(dates);
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
        }return date;
    }
    
    public ArrayList<Integer> getCases(String name, String startDate,String endDate) {
        Connection connection = null;
        ArrayList<Integer>cases = new ArrayList<Integer>();
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT nocases FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE c.cname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "'); ORDER BY date";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String dates = results.getString("nocases");
                int allcases = Integer.parseInt(dates);
                cases.add(allcases);
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
        }return cases;
    }
    
    public ArrayList<String> getStateDate(String name, String startDate,String endDate) {
        Connection connection = null;
        ArrayList<String>date = new ArrayList<String>();
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Strftime('%d/%m/%Y',date) AS 'date' FROM State s JOIN Statecount ON s.stateId = statecount.stateID"
                             + " WHERE s.sname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "'); ORDER BY date";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String dates = results.getString("date");
                date.add(dates);
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
        }return date;
    }
    
    public ArrayList<Integer> getStateCases(String name, String startDate,String endDate) {
        Connection connection = null;
        ArrayList<Integer>cases = new ArrayList<Integer>();
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT nocases FROM State s JOIN Statecount ON s.stateId = statecount.stateID"
            + " WHERE s.sname = '" + name + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "'); ORDER BY date";

            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String dates = results.getString("nocases");
                int allcases = Integer.parseInt(dates);
                cases.add(allcases);
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
        }return cases;
    }
    
    public long globalPopulation() {
        Connection connection = null;
        long pop = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(population) FROM Country;";
                             
            ResultSet results = statement.executeQuery(query);
            String population = results.getString("SUM(population)");
            pop = Long.parseLong(population);
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
        return pop;
    }
    
    public int getGlobalCases(String startDate,String endDate) {
        Connection connection = null;
        int sumCases = 0;
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT SUM(Nocases) FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE date BETWEEN '"+ startDate + "' AND '"+ endDate + "'";

            ResultSet results = statement.executeQuery(query);
            String cases = results.getString("SUM(Nocases)");
            sumCases = Integer.parseInt(cases);
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
        return sumCases;
    }
    
    public String getGlobalDate(String startDate,String endDate) {
        Connection connection = null;
        String highDate = "";
        try {
            connection = DriverManager.getConnection(DATABASE);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            String query = "SELECT Strftime('%d/%m/%Y',date) AS 'date' FROM Country c JOIN CountryCases ON c.CountryId = CountryCases.countryID"
                             + " WHERE date BETWEEN '"+ startDate + "' AND '"+ endDate + "'GROUP BY date ORDER BY date DESC LIMIT 1";
            ResultSet results = statement.executeQuery(query);
            highDate = results.getString("date");
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
            }return highDate;
        }
        
        public ArrayList<String> getSimilarCountries(String country,int distance) {
            ArrayList<String> countries = new ArrayList<String>();
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DATABASE);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                String query = "SELECT * FROM Country WHERE cname = '"+country + "'"; 
                ResultSet results = statement.executeQuery(query);
                int lat = results.getInt("Latitude");
                int longitude = results.getInt("Longitude");
                String query2 = "SELECT * FROM("
                    +"SELECT *,(((acos(sin(("+ lat + "*pi()/180)) * sin((Latitude*pi()/180))+cos(("+lat+"*pi()/180)) * cos((Latitude*pi()/180)) * cos((("+longitude + " - Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM country)"
                +"WHERE distance <=" + distance +" AND cname <> '"+ country + "' ORDER BY distance";
                ResultSet results2 = statement.executeQuery(query2);
               
                while (results2.next()) {
                    String countryName = results2.getString("cname");
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
        
        public ArrayList<Double> getSimCountDis(String country,int distance) {
            ArrayList<Double> distances = new ArrayList<Double>();
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DATABASE);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                String query = "SELECT * FROM Country WHERE cname = '"+country + "'"; 
                ResultSet results = statement.executeQuery(query);
                int lat = results.getInt("Latitude");
                int longitude = results.getInt("Longitude");
                String query2 = "SELECT * FROM("
                    +"SELECT *,(((acos(sin(("+ lat + "*pi()/180)) * sin((Latitude*pi()/180))+cos(("+lat+"*pi()/180)) * cos((Latitude*pi()/180)) * cos((("+longitude + " - Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM country)"
                +"WHERE distance <=" + distance +" AND cname <> '"+ country + "' ORDER BY distance";
                ResultSet results2 = statement.executeQuery(query2);
               
                while (results2.next()) {
                    double totDistance = results2.getDouble("distance");
                    distances.add(totDistance);
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
            return distances;
        }
        
        public ArrayList<String> getSimilarClimate(String country) {
            ArrayList<String> countries = new ArrayList<String>();
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DATABASE);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                String query = "SELECT * FROM Country WHERE cname = '"+country + "'"; 
                ResultSet results = statement.executeQuery(query);
                int lat = results.getInt("Latitude");
                String query2 = "SELECT * FROM Country WHERE cname <> '"+ country + "' ORDER BY ABS(" + lat + " - latitude) ASC LIMIT 5";
                ResultSet results2 = statement.executeQuery(query2);
               
                while (results2.next()) {
                    String countryName = results2.getString("cname");
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
        
        public ArrayList<Double> getSimClimDis(String country) {
            ArrayList<Double> distances = new ArrayList<Double>();
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(DATABASE);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                String query = "SELECT * FROM Country WHERE cname = '"+ country + "'"; 
                ResultSet results = statement.executeQuery(query);
                int lat = results.getInt("Latitude");
                int longitude = results.getInt("Longitude");
                String query2 = "SELECT * FROM("
                    +"SELECT *,(((acos(sin(("+ lat + "*pi()/180)) * sin((Latitude*pi()/180))+cos(("+lat+"*pi()/180)) * cos((Latitude*pi()/180)) * cos((("+longitude + " - Longitude)*pi()/180))))*180/pi())*60*1.1515*1.609344) as distance FROM country)"
                +"WHERE cname <> '"+ country + "' ORDER BY ABS(" + lat + " - latitude) ASC LIMIT 5";
                ResultSet results2 = statement.executeQuery(query2);
               
                while (results2.next()) {
                    double totDistance = results2.getDouble("distance");
                    distances.add(totDistance);
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
            return distances;
        }

//DOMS CLASSES
    //return total amount of cases worldwide
    public ArrayList<Integer> totalCases() {
        ArrayList<Integer> covid = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM CountryCases";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                int noCases = results.getInt("NoCases");

                // For now we will just store the movieName and ignore the id
                covid.add(noCases);
            }
            
            //add all of each countries cases to get total cases
            // int totalCases = 0;
            // for (int i: covid) {
            //     totalCases = totalCases + i;
            // }


            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        ////return the integer list containing all values of 'nocases' 
        return covid;
    }

    // return total number of deaths globally 
    public ArrayList<Integer> totalDeaths() {
        ArrayList<Integer> covid = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM CountryCases";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                int noDeaths = results.getInt("NoDeaths");

                // For now we will just store the movieName and ignore the id
                covid.add(noDeaths);
            }
            
            //add all of each countries cases to get total cases
            // int totalCases = 0;
            // for (int i: covid) {
            //     totalCases = totalCases + i;
            // }


            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the integer list containing all values of 'nodeaths' 
        return covid;
    }

    // return total number of deaths globally 
    public ArrayList<Integer> countryTotalDeaths(String country) {
        ArrayList<Integer> covid = new ArrayList<Integer>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //The Query
            String query = "SELECT c.Cname, SUM(cc.noDeaths) FROM Country c JOIN CountryCases cc ON cc.CountryID = c.CountryID WHERE c.cname='" + country + "';";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                int noDeaths = results.getInt("SUM(cc.NoDeaths)");

                // For now we will just store the movieName and ignore the id
                covid.add(noDeaths);
            }
            
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the integer list containing all values of 'nodeaths' 
        return covid;
    }

    //this adds all cases within each country and orders them in an arraylist by number of cases
    public ArrayList<String> mostCases() {
        ArrayList<String> covid = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT c.Cname, SUM(cc.NoCases) AS \"NoCases\" FROM CountryCases cc JOIN Country c ON c.countryID = cc.countryID GROUP BY cc.countryID ORDER BY NoCases DESC";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String noCases = results.getString("NoCases");
                String cName = results.getString("Cname");

                // For now we will just store the movieName and ignore the id
                covid.add(cName);
                covid.add(noCases); 
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the value of total cases
        return covid;
    }

    //this adds all deaths within each country and orders them in an arraylist by number of deaths
    public ArrayList<String> mostDeaths() {
        ArrayList<String> covid = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT c.Cname, SUM(cc.NoDeaths) AS \"NoDeaths\", SUM(cc.NoCases) AS \"NoCases\", SUM(cc.NoDeaths) * 1.0 / SUM(cc.NoCases) * 100 AS \"DeathRate\" FROM CountryCases cc JOIN Country c ON c.countryID = cc.countryID GROUP BY cc.countryID ORDER BY NoDeaths DESC;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String noDeaths = results.getString("NoDeaths");
                String cName = results.getString("Cname");

                // For now we will just store the movieName and ignore the id
                covid.add(cName);
                covid.add(noDeaths); 
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the value of total cases
        return covid;
    }

    //this calculates the death rate for each country and adds them into an arraylist ordered by death rate descending
    public ArrayList<String> highestDeathRate() {
        ArrayList<String> covid = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // query orders death rate from highest to lowest within the arraylist
            String query = "SELECT c.Cname, SUM(cc.NoDeaths) AS \"NoDeaths\", SUM(cc.NoCases) AS \"NoCases\", SUM(cc.NoDeaths) * 1.0 / SUM(cc.NoCases) * 100 AS \"DeathRate\" FROM CountryCases cc JOIN Country c ON c.countryID = cc.countryID GROUP BY cc.countryID ORDER BY DeathRate DESC;";
            
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String cName = results.getString("Cname");
                Float deathRate = results.getFloat("DeathRate");
                String stringDeathRate = String.valueOf(deathRate);
                

                // For now we will just store the movieName and ignore the id
                covid.add(cName);
                covid.add(stringDeathRate);
            }
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the value of total cases
        return covid;
    }

    //return value of most deaths on a day (index 0) and the date of highest deaths (index 1)
    public ArrayList<String> highestDeathDay(String country) {
        ArrayList<String> covid = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //The Query
            String query = "SELECT Date, MAX(noDeaths) FROM Country c JOIN CountryCases cc ON cc.CountryID = c.CountryID WHERE c.cname='" + country + "';";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String noDeaths = results.getString("MAX(NoDeaths)");
                String date = results.getString("Date");

                covid.add(noDeaths);
                covid.add(date);
            }
            
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        //return the integer list containing all values of 'nodeaths' 
        return covid;
    }

    //return the current mortality rate of COVID in the selected country
    public ArrayList<Float> countryDeathRate(String country) {
        ArrayList<Float> covid = new ArrayList<Float>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //The Query
            String query = "SELECT SUM(NoCases), SUM(NoDeaths) FROM Country c JOIN CountryCases cc ON cc.CountryID = c.CountryID WHERE c.cname='" + country + "';";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                float noDeaths = results.getFloat("SUM(NoDeaths)");
                float noCases = results.getFloat("SUM(NoCases)");

                covid.add(noDeaths);
                covid.add(noCases);
            }
            
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return covid;
    }  

    //this retrieves the total deaths of a specified country within a date range
    public ArrayList<Float> getDeathsDateRange(String startDate, String endDate, String country) {
        ArrayList<Float> covid = new ArrayList<Float>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //The Query
            String query = "SELECT SUM(NoCases), SUM(NoDeaths) FROM Country c JOIN CountryCases cc ON cc.CountryID = c.CountryID WHERE c.cname='" + country + "' AND (date BETWEEN '"+ startDate + "' AND '"+ endDate + "');";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                float noDeaths = results.getFloat("SUM(NoDeaths)");
                float noCases = results.getFloat("SUM(NoCases)");

                covid.add(noDeaths);
                covid.add(noCases);
            }
            
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return covid;
    }  

    //doesnt work rn
    public ArrayList<Float> getSimilarDeathRate(String country) {
        ArrayList<Float> covid = new ArrayList<Float>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            //sort by determining index of selected country, then use the indexes around that value
            String query = "SELECT (SUM(cc.NoDeaths) * 1.0 / SUM(cc.NoCases) * 100) AS \"DeathRate\" FROM CountryCases cc JOIN Country c ON c.countryID = cc.countryID WHERE c.cname='" + country + "';";
            // ResultSet results2 = statement.executeQuery(query);
            // Float selectedCountryDeathRate = (results2.getFloat("DeathRate"));

            //TODO: i dont know what to do uhuhuhuhuhhhh but try what luke said

            //The Query
            query = "SELECT c.Cname, SUM(cc.NoDeaths) AS \"NoDeaths\", SUM(cc.NoCases) AS \"NoCases\", SUM(cc.NoDeaths) * 1.0 / SUM(cc.NoCases) * 100 AS \"DeathRate\" FROM CountryCases cc JOIN Country c ON c.countryID = cc.countryID GROUP BY cc.countryID ORDER BY DeathRate DESC;";
            
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!

                float deathRate = results.getFloat("DeathRate");

                covid.add(deathRate);

                // System.out.println(deathRate);
            }

            
            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return covid;
    }

}
