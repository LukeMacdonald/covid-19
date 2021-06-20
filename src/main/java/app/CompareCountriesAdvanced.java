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

        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>";
        html = html + "<link href='https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css' rel='stylesheet' />";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js'></script>";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";

        html = html + "<script>";
        //Creates a searchable dropdown menu
        html = html + "$(document).ready(function(){";
        html = html + "$('#country_drop_1').select2();";
        html = html + "});";
        //Creates a searchable dropdown menu
        html = html + "$(document).ready(function(){";
        html = html + "$('#country_drop_2').select2();";
        html = html + "});";
        //Function to set the end dates min value to the start dates input
        html = html + "function updatedate() {";
        html = html + " var firstdate = document.getElementById('startDate').value;";
        html = html + " document.getElementById('endDate').value = '';";
        html = html + " document.getElementById('endDate').setAttribute('min',firstdate);";
        html = html + "}";
        html = html + "</script>";

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
        html = html + "<a href='/cumalative_global.html''>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html' style = 'background-color:#3189af'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/comparecountries.html'>Compare Countries</a> > <a href='/comparecountriesadvanced.html'>Compare Countries: Advanced</a></p>"; 
        html = html + "<h1>Find Similar Countries";
        html = html + "<div style='display: flex; justify-content:left;'>";
        html = html + "<button onclick=\"document.location='/comparecountries.html'\" style='margin-top:10px;font-size:16px;'; type='button'; >Simple Compare</button>";
        html = html + "</div>";
        html = html + "</h1>";
        html = html + "<h5>Using this advanced compare form, directly compare stats and information between two countries</h5>";
        html = html + "</div>";


        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> countries = jdbc.getCountries();
       

        //navbar to switch between states and countries
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/comparecountriesadvanced.html' style = 'background-color: rgb(241, 241, 241)'>Compare Countries</a>";
        html = html + " <a href='/comparestatesadvanced.html'>Compare States</a>";
        html = html + " <a href='/comparestatecountryadvanced.html'>Compare State to Country</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";

        //start of form and inputs to select country 1 and other parameters;
                    
    
        
        
        //Form
        html = html + "<form class = action='/comparecountriesadvanced.html' method='post'>";
        html = html + " <div id = 'location_selector'>";
        html = html + "<label for='country_drop_1' id = 'label1'>Select Country: </label><br>";
            html = html + "<select id='country_drop_1' name='country_drop_1' value = 'Australia'>";
        html = html + "   <option selected disabled>Select a Country...</option>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + " </select>";
        html = html + "<label for='country_drop_2' id = 'label1'>Select Country: </label><br>";
            html = html + "<select id='country_drop_2' name='country_drop_2' value = 'Australia'>";
            html = html + "    <option selected disabled>Select a Country...</option>";
            for(int i = 0; i < countries.size();i++){
                html = html + "<option >" + countries.get(i) + "</option>";
            }
            html = html + "</select>";
        html = html + "</div>";
        html = html + " <div id = 'date_selector'>";
        html = html + " <label for = 'startDate'>Select Start Date:</label>";
        html = html + " <input class='form-control' type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22'onchange= 'updatedate()' value = '2020-01-22'> ";
        html = html + "  <label for = 'endDate'>Select End Date:</label>";
        html = html + "  <input class='form-control' id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'>";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";

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

        
            ArrayList<Integer> countryDeathsDateRange_1 = jdbc.getDeathsDateRange(startDate, endDate, country_drop_1);
                            int noDeathsDateRange_1 = countryDeathsDateRange_1.get(0);
                            int noCasesDateRange_1 = countryDeathsDateRange_1.get(1);
                            double deathRateDateRange_1 = noDeathsDateRange_1 * 1.0 / noCasesDateRange_1 * 100;

            ArrayList<Integer> countryDeathsDateRange_2 = jdbc.getDeathsDateRange(startDate, endDate, country_drop_2);
                            int noDeathsDateRange_2 = countryDeathsDateRange_2.get(0);
                            int noCasesDateRange_2 = countryDeathsDateRange_2.get(1);
                            double deathRateDateRange_2 = noDeathsDateRange_2 * 1.0 / noCasesDateRange_2 * 100;

            String highestDeathDay1 = jdbc.highestDeathDay(country_drop_1, startDate, endDate).get(1);
            String highestDeaths1 = jdbc.highestDeathDay(country_drop_1, startDate, endDate).get(0);

            String highestDeathDay2 = jdbc.highestDeathDay(country_drop_2, startDate, endDate).get(1);
            String highestDeaths2 = jdbc.highestDeathDay(country_drop_2, startDate, endDate).get(0);
            

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
    
                    html = html + "<div id='cases1'>";
                    //total cases recorded in time period
                            html = html + "<p>" + country_drop_1 + " total cases: </p>";
                            html = html + "<p>" + noCasesDateRange_1 + "</p>";
                
                    html = html + "</div>";
    
                    html = html + "<div id='cases2'>";
                    //total cases recorded in time period
                            html = html + "<p>" + country_drop_2 + " total cases: </p>";
                            html = html + "<p>" + noCasesDateRange_2 + "</p>";
                
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

                html = html + "<div id='highestDeaths1'>";
                //highest deaths and date of recorded by selected country 1
                    html = html + "<p>" + country_drop_1 + " highest deaths in a day: </p>";
                        html = html + "<p>" + highestDeaths1 + "</p>";
                        html = html + "<p>" + country_drop_1 + "'s date of highest deaths': </p>";
                        html = html + "<p>" + highestDeathDay1 + "</p>";
            
                html = html + "</div>";

                html = html + "<div id='highestDeaths2'>";
                //highest deaths and date of recorded by selected country 2
                html = html + "<p>" + country_drop_2 + " highest deaths in a day: </p>";
                html = html + "<p>" + highestDeaths2 + "</p>";
                html = html + "<p>" + country_drop_2 + "'s date of highest deaths': </p>";
                html = html + "<p>" + highestDeathDay2 + "</p>";

            
        }

        //ends compare-flex-container div
        html = html + "</div>";

        // Finish the HTML webpage
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
