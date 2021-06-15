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
public class CompareCountriesAdvanced implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/comparecountriesadvanced.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Compare Countries</title>" +
               "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";

        // Add the body
        html = html + "<body>";

        //navbar done by luke
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html' style = 'background-color:#3189af'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title2'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/comparecountries.html'>Compare Countries</a> > <a href='/comparecountriesadvanced.html'>Compare Countries: Advanced</a></p>";  
        html = html + "<h1>Compare Countries: Advanced";
        //button to return to simple compare page
        html = html + "<div style='display: flex; justify-content:left;'>";
        html = html + "<button onclick=\"document.location='/comparecountries.html'\" style='margin-top:10px;' type='button';'>Simple Search</button>";
        html = html + "</div>";
        
        html = html + "</h1>";
        html = html + "<h5>Using this advanced compare form, directly compare stats and information between two countries</h5>";
        html = html + "</div>";


        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> countries = jdbc.getCountries();


        //flex container for the content of compare page
        html = html + "<div id='compare-flex-container'>";
        html = html + "<div class='search-sidebar'>";
        //same location selector class as on the country deaths page
        html = html + "<div class='location-selector-2'>";

        //navbar to switch between states and countries
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/comparecountriesadvanced.html' style = 'background-color: rgb(241, 241, 241)'>Compare Countries</a>";
        html = html + " <a href='/comparestatesadvanced.html'>Compare States</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";

        //start of form and inputs to select country 1 and other parameters
        html = html + "<form action='/comparecountriesadvanced.html' method='post'>";
                    
            html = html + "<label for='country_drop_1' id = 'label1'>Select Country: </label><br>";
            html = html + "<select id='country_drop_1' name='country_drop_1' value = 'Australia'>";
            html = html + "    <option selected disabled>Select a Country...</option>";
            for(int i = 0; i < countries.size();i++){
                html = html + "<option >" + countries.get(i) + "</option>";
            }
            html = html + "</select><br>";
                   
        //inputs to select country 2 and other parameters
                    
            html = html + "<label for='country_drop_2' id = 'label1'>Select Country: </label><br>";
            html = html + "<select id='country_drop_2' name='country_drop_2' value = 'Australia'>";
            html = html + "    <option selected disabled>Select a Country...</option>";
            for(int i = 0; i < countries.size();i++){
                html = html + "<option >" + countries.get(i) + "</option>";
            }
            html = html + "</select><br>";

        //date range selection
        html = html + " <label for = 'startDate'>Select Start Date:</label><br>";
        html = html + " <input type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22'onchange= 'updatedate()' value = '2020-01-22'><br>";
        html = html + "  <label for = 'endDate'>Select End Date:</label><br>";
        html = html + "  <input id='endDate_1' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'><br>";
        
        //buttons to sumbit and reset the form
            html = html + "<button type='submit' id = 'submit'>Show Data</button><br>";
            html = html + "<input type='reset' id = 'reset'>";
        html = html + "</form>";

        //ends location-selector div
        html = html + "</div>";
        //ends search-sidebar div
        html = html + "</div>";

        //gets form input through post
        String country_drop_1 = context.formParam("country_drop_1");
        String country_drop_2 = context.formParam("country_drop_2");
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate"); 

        
        //if no country is selected, no output is displayed on the page
        if(country_drop_1 == null || country_drop_2 == null){

            html = html + "";
        }
        //creates output on page with data
        else {

            
            int countryTotalDeaths_1 = jdbc.countryTotalDeaths(country_drop_1);
            ArrayList<String> highestDeathDay_1 = jdbc.highestDeathDay(country_drop_1);

            ArrayList<Float> countryDeathsDateRange_1 = jdbc.getDeathsDateRange(startDate, endDate, country_drop_1);
                            float noDeathsDateRange_1 = countryDeathsDateRange_1.get(0);
                            float noCasesDateRange_1 = countryDeathsDateRange_1.get(1);
                            double deathRateDateRange_1 = noDeathsDateRange_1 / noCasesDateRange_1 * 100;

            int countryTotalDeaths_2 = jdbc.countryTotalDeaths(country_drop_2);
            ArrayList<String> highestDeathDay_2 = jdbc.highestDeathDay(country_drop_2);

            ArrayList<Float> countryDeathsDateRange_2 = jdbc.getDeathsDateRange(startDate, endDate, country_drop_2);
                            float noDeathsDateRange_2 = countryDeathsDateRange_2.get(0);
                            float noCasesDateRange_2 = countryDeathsDateRange_2.get(1);
                            double deathRateDateRange_2 = noDeathsDateRange_2 / noCasesDateRange_2 * 100;
            

                html = html + "<div id='results-flex-container'>";

                //this provides an explaination at the top of the results div as to what the data below represents
                html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Comparing " + country_drop_1 + " to " + country_drop_2 + "</h2>";
                    html = html + "<h5>The below results compare COVID-19 stats and information between " + country_drop_1 
                                    + " and " + country_drop_2
                                    + "</h5>";
                html = html + "</div>";

            html = html + "<div id='deaths1'>";
                    //total deaths recorded by selected country 1
                    html = html + "<p>" + country_drop_1 + " total deaths: </p>";
                        html = html + "<p>" + noDeathsDateRange_1 + "</p>";
                
                html = html + "</div>";

                html = html + "<div id='deaths2'>";
                    //total deaths recorded by selected country 2
                    html = html + "<p>" + country_drop_2 + " total deaths: </p>";
                        html = html + "<p>" + noDeathsDateRange_2 + "</p>";

                html = html + "</div>";
            
            html = html + "<div class='break' style='border:none; background-color: transparent;'></div>";

                html = html + "<div id='deathRate1'>";
                //total deaths recorded by selected country 1
                double deathRate1 = Math.round(deathRateDateRange_1*100.0)/100.0;
                    html = html + "<p>" + country_drop_1 + " mortality rate: </p>";
                        html = html + "<p>" + deathRate1 + "%</p>";
            
                html = html + "</div>";

                html = html + "<div id='deathRate2'>";
                    //total deaths recorded by selected country 2
                    double deathRate2 = Math.round(deathRateDateRange_2*100.0)/100.0;
                    html = html + "<p>" + country_drop_2 + " mortality rate: </p>";
                        html = html + "<p>" + deathRate2 + "%</p>";

                html = html + "</div>";

            html = html + "<div class='break' style='border:none; background-color: transparent;'></div>";

                html = html + "<div id='deathPerMillion1'>";
                //total deaths recorded by selected country 1
                int population_1 = jdbc.population(country_drop_1);
                double deathPerMillion1 = Math.round((noDeathsDateRange_1 * 1.0 / (population_1/100000))*100.0)/100.0;
                    html = html + "<p>" + country_drop_1 + " deaths per hundred thousand: </p>";
                        html = html + "<p>" + deathPerMillion1 + "</p>";
            
                html = html + "</div>";

                html = html + "<div id='deathPerPopulation2'>";
                    //total deaths recorded by selected country 2
                    int population_2 = jdbc.population(country_drop_2);
                    double deathPerMillion2 = Math.round((noDeathsDateRange_2 * 1.0 / (population_2/100000))*100.0)/100.0;
                    html = html + "<p>" + country_drop_2 + " deaths per hundred thousand: </p>";
                        html = html + "<p>" + deathPerMillion2 + "</p>";

                html = html + "</div>";
        }       //TODO: Add rest of required results on spec sheet, then make state version of page

        //ends compare-flex-container div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
