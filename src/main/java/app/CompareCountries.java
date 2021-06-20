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
public class CompareCountries implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/comparecountries.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>";
        html = html + "<link href='https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css' rel='stylesheet' />";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js'></script>";

        html = html + "<script>";
        //Creates a searchable dropdown menu
        html = html + "$(document).ready(function(){";
        html = html + "$('#country_drop').select2();";
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
        html = html + "<a href='/cumulative_report.html'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html' style = 'background-color:#3189af'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title2'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/comparecountries.html'>Compare Countries</a></p>"; 
        html = html + "<h1>Find Similar Countries";
        html = html + "<div style='display: flex; justify-content:left;'>";
        html = html + "<button onclick=\"document.location='/comparecountriesadvanced.html'\" style='margin-top:10px;' type='button';'>Compare 2 Countries</button>";
        html = html + "</div>";
        html = html + "</h1>";
        html = html + "<h5>Using this simple compare form, find countries similar to that selected. If you want to compare 2 countries, click the \"Compare 2 Countries\" button</h5>";
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
        html = html + " <a href='/comparecountries.html' style = 'background-color: rgb(241, 241, 241)'>Compare Countries</a>";
        html = html + " <a href='/comparestates.html'>Compare States</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";

        html = html + "<form action='/comparecountries.html' method='post'>";
                    
            html = html + "<label for='country_drop' id = 'label1'>Select Country: </label><br>";
            html = html + "<select id='country_drop' name='country_drop' value = 'Australia'>";
            html = html + "    <option selected disabled>Select a Country...</option>";
            for(int i = 0; i < countries.size();i++){
                html = html + "<option >" + countries.get(i) + "</option>";
            }
            html = html + "</select><br>";
            
            //date range selection
            html = html + " <label for = 'startDate'>Select Start Date:</label><br>";
            html = html + " <input type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22'onchange= 'updatedate()' value = '2020-01-22'><br>";
            html = html + "  <label for = 'endDate'>Select End Date:</label><br>";
            html = html + "  <input id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'><br>";

            //buttons to sumbit and reset the form
            html = html + "<button type='submit' id = 'submit'>Show Data</button>";
            html = html + "<input type='reset' id = 'reset'>";
        html = html + "</form>";

        //ends location-selector div
        html = html + "</div>";
        //ends search-sidebar div
        html = html + "</div>";

        //gets form input through post
        String country_drop = context.formParam("country_drop");
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        
        //if no country is selected, no output is displayed on the page
        if(country_drop == null){

            html = html + "";
        }
        //creates output on page with data
        else {

            ArrayList<String> similarDeathRate = jdbc.getSimilarDeathRate(country_drop, startDate, endDate);
            ArrayList<String> similarHighestDeaths = jdbc.similarHighestDeaths(country_drop, startDate, endDate);
            ArrayList<String> similarTotalDeathsCountry = jdbc.similarTotalDeathsCountry(country_drop, startDate, endDate);
            ArrayList<String> similarTotalCasesCountry = jdbc.similarTotalCasesCountry(country_drop, startDate, endDate);

                html = html + "<div id='results-flex-container'>";

                //this provides an explaination at the top of the results div as to what the data below represents
                html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Similar Countries to " + country_drop + "</h2>";
                    html = html + "<h5>The below results display COVID-19 stats similar to those of " + country_drop
                                    + "</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    //similar death rates to selected country within time period
                    html = html + "<p style='font-weight:bolder'>3 Most similar mortality rates: </p>";
                    
                    for (int i=0; i < similarDeathRate.size(); i=i+2){

                        float deathRate = Float.parseFloat(similarDeathRate.get(i+1));
                        double deathRate2d = Math.round(deathRate*100.0)/100.0;
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarDeathRate.get(i) + ":</p>";
                        html = html + deathRate2d;
                        html = html + "%<br>";
                    }



                html = html + "</div>";

                html = html + "<div id='div2'>";
                    //closest highest death day
                    html = html + "<p style='font-weight:bolder'>3 Most similar highest death day: </p>";
                    
                    for (int i=0; i < similarHighestDeaths.size(); i=i+3){

                        String maxNoDeaths = similarHighestDeaths.get(i+1);
                        String maxDeathDate = similarHighestDeaths.get(i+2);
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarHighestDeaths.get(i) + ":</p>";
                        html = html + maxNoDeaths + " deaths <br>on " + maxDeathDate;
                        html = html + "<br>";
                    }
                    //TODO: take a look at what Luke said in the teams to get the similar values working




                html = html + "</div>";

                html = html + "<div id='div3'>";
                    //similar total deaths
                    html = html + "<p style='font-weight:bolder'>3 Most similar total deaths: </p>";
                    
                    for (int i=0; i < similarTotalDeathsCountry.size(); i=i+2) {
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalDeathsCountry.get(i) + ":</p>";
                        html = html + similarTotalDeathsCountry.get(i+1) + " deaths";
                    }  
                        
                    
                    html = html + "</div>";

                    html = html + "<div id='div4'>";
                //similar total cases
                html = html + "<p style='font-weight:bolder'>3 Most similar total cases: </p>";
                
                for (int i=0; i < similarTotalCasesCountry.size(); i=i+2) {
                    html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalCasesCountry.get(i) + ":</p>";
                    html = html + similarTotalCasesCountry.get(i+1) + " cases";
                }  
                    
                
                html = html + "</div>";
                        
                    
                    html = html + "</div>";
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
