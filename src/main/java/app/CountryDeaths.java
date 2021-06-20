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
public class CountryDeaths implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/countrydeaths.html";

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
               "<title>Deaths by Country</title>" +
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
        html = html + "<a href='/cumalative_global.html''>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        //header
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px;'><a href = '/'>Home</a> > <a href = '/infection_global.html'>Death Rates</a></p>"; 
        html = html + "<h1>Global Deaths by Country</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>Select a country below to view deaths and related information</h5>";
        html = html + "</div>";


        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> countries = jdbc.getCountries();

        //form, including country selection
        // TODO: make ^ searchable
    
        html = html + "<div id='navbar2'>";
        html = html + "<a href = '/countrydeaths.html'style = 'background-color: rgb(241, 241, 241)'>Country Deaths</a>";
        html = html + "<a href='/statedeaths.html'>State Deaths</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
        
        //Form
        html = html + "<form class = action='/countrydeaths.html' method='post'>";
        html = html + " <div id = 'location_selector'>";
        html = html + "<label for='country_drop' id = 'label1'>Select Country:</label><br>";
        html = html + "<select id='country_drop' name='country_drop' value = 'Australia' required>";
        html = html + "   <option selected disabled>Select a Country...</option>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + " </select>";
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
        String country_drop = context.formParam("country_drop");   
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate"); 
        
        //if no country is selected, no output is displayed on the page
        if(country_drop == null){

            html = html + "";
        }
        //creates output on page with data
        else {

            // ArrayList<Integer> countryTotalDeaths = jdbc.countryTotalDeaths(country_drop);
            ArrayList<String> highestDeathDay = jdbc.highestDeathDay(country_drop, startDate, endDate);
            // ArrayList<Float> countryDeathRate = jdbc.countryDeathRate(country_drop);

                html = html + "<div class = 'container1' id='results-flex-container'>";

                html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Deaths in " + country_drop + "</h2>";
                    html = html + "<h5>The below results represent the total deaths and other information from " + country_drop 
                                    + " within the determined time frame. If the time frame is not altered, this covers the total"
                                    + " values from the whole of the COVID-19 pandemic</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    ArrayList<Integer> countryDeathsDateRange = jdbc.getDeathsDateRange(startDate, endDate, country_drop);
                        int noDeathsDateRange = countryDeathsDateRange.get(0);
                        int noCasesDateRange = countryDeathsDateRange.get(1);
                        double deathRateDateRange = noDeathsDateRange * 1.0 / noCasesDateRange * 100;
                        
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
                    
                        html = html + highestDeathDay.get(0);
                    
                    //date of highest recorded deaths
                    html = html + "<p>Date of highest recorded deaths: </p>";

                        html = html + highestDeathDay.get(1);
                
                    //ends div3
                html = html + "</div>";
                        
                html = html + "<div id='div4'>";
                
                        double deathRate2d = Math.round(deathRateDateRange*100.0)/100.0;
                        html = html + "<p>Mortality rate in date range: </p>";
                        html = html + deathRate2d;
                        html = html + "%<br>";
                    
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