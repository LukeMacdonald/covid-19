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
public class StateDeaths implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/statedeaths.html";

    @Override
    public void handle(Context context) throws Exception {

        // Create a simple HTML webpage in a String
        var html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Deaths by State</title>" +
               "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css'/>";

        // Add the body
        html = html + "<body>";

        //navbar done by luke
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html' style = 'background-color:#3189af'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title2'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/countrydeaths.html'>Deaths by Country</a> > <a href = '/statedeaths.html'> Deaths by State</a></p>"; 
        html = html + "<h1>Global Deaths by State</h1>";
        html = html + "<h5>Select a state below to view deaths and related information</h5>";
        html = html + "</div>";


        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> states = jdbc.getStates();

        //form, including country selection
        // TODO: make ^ searchable
    
        html = html + "<div id='deaths-page-flex-container'>";
        html = html + "<div class='search-sidebar'>";
        html = html + "<div class='location-selector-2'>";

        //navbar to switch between states and countries
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/countrydeaths.html' >Deaths by Country</a>";
        html = html + " <a href='/statedeaths.html'style = 'background-color: rgb(241, 241, 241)'>Deaths by State</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";

            html = html + "<form action='/statedeaths.html' method='post'>";
                
            //TODO: add way to switch between selecting states/regions and countries
                html = html + "<label for='country_drop' id = 'label1'>Select State: </label><br>";
                html = html + "<select id='country_drop' name='country_drop' value = 'Victoria'>";
                html = html + "    <option selected disabled>Select a State...</option>";
                for(int i = 0; i < states.size();i++){
                    html = html + "<option >" + states.get(i) + "</option>";
                }
                html = html + "</select><br>";
                
                //date range selection
                html = html + " <label for = 'startDate'>Select Start Date:</label><br>";
                html = html + " <input type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22'onchange= 'updatedate()' value = '2020-01-22'><br>";
                html = html + "  <label for = 'endDate'>Select End Date:</label><br>";
                html = html + "  <input id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'><br>";

                //buttons to sumbit and reset the form
                html = html + "<button type='submit' id = 'submit'>Show Data</button><br>";
                html = html + "<input type='reset' id = 'reset'>";
            html = html + "</form>";

            //ends location selector div
            html = html + "</div>";
            //ends search-sidebar flex container
            html = html + "</div>";
            //deaths-page-flex-container ends later as it still must contain the results

        //gets form input through post
        String country_drop = context.formParam("country_drop");   
        
        //if no country is selected, no output is displayed on the page
        if(country_drop == null){

            html = html + "";
        }
        //creates output on page with data
        else {

            // ArrayList<Integer> countryTotalDeaths = jdbc.countryTotalDeaths(country_drop);
            ArrayList<String> highestDeathDayState = jdbc.highestDeathDayState(country_drop);
            // ArrayList<Float> countryDeathRate = jdbc.countryDeathRate(country_drop);

                html = html + "<div id='results-flex-container'>";

                html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Deaths in " + country_drop + "</h2>";
                    html = html + "<h5>The below results represent the total deaths and other information from " + country_drop 
                                    + " within the determined time frame. If the time frame is not altered, this covers the total"
                                    + " values from the whole of the COVID-19 pandemic</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    String startDate = context.formParam("startDate");
                    String endDate = context.formParam("endDate");
                    ArrayList<Float> stateDeathsDateRange = jdbc.getDeathsDateRangeState(startDate, endDate, country_drop);
                        float noDeathsDateRange = stateDeathsDateRange.get(0);
                        float noCasesDateRange = stateDeathsDateRange.get(1);
                        float deathRateDateRange = noDeathsDateRange / noCasesDateRange * 100;
                        
                    html = html + "<p>Total deaths in date range: </p>";
                    html = html + noDeathsDateRange;
                html = html + "</div>";

                html = html + "<div id='div2'>";                     
                    html = html + "<p>Total cases in date range: </p>";
                    html = html + noCasesDateRange;
                html = html + "</div>";

                html = html + "<div id='div3'>";
                    //highest recorded deaths in a day
                    html = html + "<p>Highest recorded deaths in a day: </p>";
                    
                        html = html + highestDeathDayState.get(0);
                    
                    //date of highest recorded deaths
                    html = html + "<p>Date of highest recorded deaths: </p>";

                        html = html + highestDeathDayState.get(1);
                
                    //ends div3
                html = html + "</div>";
                        
                html = html + "<div id='div4'>";
                    
                    html = html + "<p>Mortality rate in date range: </p>";
                    
                        html = html + deathRateDateRange + "%";
                    
                    html = html + "</div>";
            
                
                //ends deaths-results-flex-container    
                html = html + "</div>";
            //ends deaths-page-flex-container
            html = html + "</div>";

        }
        

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
