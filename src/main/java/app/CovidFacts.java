package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Temporary HTML as an example page.
 * 
 * Based on the Project Workshop code examples.
 * This page currently:
 *  - Provides a link back to the index page
 *  - Displays the list of movies from the Movies Database using the JDBCConnection
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class CovidFacts implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/covidfacts.html";

    @Override
    public void handle(Context context) throws Exception {

        //open jdbc database connection
        JDBCConnection jdbc = new JDBCConnection();

        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>COVID-19 Facts</title>" +
               "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";

        // Add the body
        html = html + "<body>";
        //navbar done by luke
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html' style = 'background-color:#3189af'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumalative_global.html''>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/covidfacts.html'>Covid Facts</a></p>"; 
        html = html + "<h1>Global COVID-19 Facts</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides easy to read covid facts</h5>";
        html = html + "</div>";

        //facts on the page
        html = html + "<p style='text-align:center'>Total COVID-19 cases globally: ";  
                //retrieve arraylist containing list of nocases
                ArrayList<Integer> casesList = jdbc.totalCases();

                //for loop to add all values in arraylist and return total number of cases
                int totalCases = 0;
                for (Integer cases : casesList) {
                    totalCases = totalCases + cases;
                }
                html = html + totalCases;
                html = html + "</p>";
        html = html + "<p style='text-align:center'>Total COVID-19 deaths globally: ";
                //retrieve arraylist containing list of nodeaths                 
                ArrayList<Integer> deathsList = jdbc.totalDeaths();

                //for loop to add all values in arraylist and return total number of deaths
                int totalDeaths = 0;
                for (Integer deaths : deathsList) {
                    totalDeaths = totalDeaths + deaths;
                }
                html = html + totalDeaths;
                html = html + "</p>";
        html = html + "<p style='text-align:center'>Total COVID-19 global mortality rate: ";
                //calculate the fraction of deaths/cases to find the 'deaths per case' value
                float totalMortality = (float)totalDeaths / totalCases * 100;
                //multiply by 100 to make a percentage. final value represents percent mortality rate
                //TODO: this value should maybe be changed to per 100 or 1000 people
                double deathRate2d = Math.round(totalMortality*100.0)/100.0;
                html = html + (deathRate2d) + "%";
                html = html + "</p>";

    
        html = html + "<div style='display: flex; justify-content:center;'>";
        html = html + "<button onclick=\"document.location='/comparecountries.html'\" id='facts_button' type='button';'>Compare by Country</button>";
        html = html + "</div>";
        
        html = html + "<div class=\"facts-flex-container\">";


        //TODO: Add buttons to each fact linking to the relevant page that provides more depth to the data. Also add hover tooltips to explain data and its relevance/meaning
        html = html + "<div><p>Highest Mortality Rate:</p>";
            ArrayList<String> deathRateList = jdbc.highestDeathRate();

            for (int i=0; i<6; i=i+2) {
                String cName = deathRateList.get(i);
                String stringDeathRate = deathRateList.get(i+1);
                Float FloatDeathRate = Float.parseFloat(stringDeathRate);
                deathRate2d = Math.round(FloatDeathRate*100.0)/100.0;
                html = html + "<p>" + cName + ": " + deathRate2d + "%</p>";
            }
            
        html = html + "</div>";    

        html = html + "<div><p>Most Cases:</p>";
            ArrayList<String> mostCasesList = jdbc.mostCases();

            for (int i=0; i<6; i=i+2) {
                html = html + "<p>" + mostCasesList.get(i) + ": " + mostCasesList.get(i+1) + "</p>";
            }
        html = html + "</div>";

        html = html + "<div><p>Most Deaths:</p>";
            ArrayList<String> mostDeathsList = jdbc.mostDeaths();

            for (int i=0; i<6; i=i+2) {
                html = html + "<p>" + mostDeathsList.get(i) + ": " + mostDeathsList.get(i+1) + "</p>";
            }
        html = html + "</div>";

        html = html + "</div>";
        

        // // Look up some information from JDBC
        

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }



}