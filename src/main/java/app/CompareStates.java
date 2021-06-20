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
public class CompareStates implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/comparestates.html";

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
               "<title>Compare States</title>" +
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
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px;text-align:left'><a href = '/'>Home</a> > <a href = '/comparecountries.html'>Compare Countries</a></p>"; 
        html = html + "<h1>Find Similar States";
        html = html + "<div style='display: flex; justify-content:left;'>";
        html = html + "<button onclick=\"document.location='/comparestatesadvanced.html'\" style='margin-top:10px;;font-size:16px;' type='button';'>Compare 2 States</button>";
        html = html + "</div>";
        html = html + "</h1>";
        html = html + "<h5>Using this simple compare form, find states similar to that selected.If you want to compare 2 states, click the \"Compare 2 States\" button</h5>";
        html = html + "</div>";


        JDBCConnection jdbc = new JDBCConnection();

        ArrayList<String> states = jdbc.getAllStates();


        

        //navbar to switch between states and countries
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/comparecountries.html'>Compare Countries</a>";
        html = html + " <a href='/comparestates.html' style = 'background-color: rgb(241, 241, 241)'>Compare States</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";

        html = html + "<form class = action='/comparestates.html' method='post'>";
        html = html + " <div id = 'location_selector'>";
        html = html + "<label for='state_drop' id = 'label1'>Select State:</label><br>";
        html = html + "<select id='state_drop' name='state_drop'>";
        html = html + "   <option selected disabled>Select a State...</option>";
        for(int i = 0; i < states.size();i++){
            html = html + "   <option >" + states.get(i) + "</option>";
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

        //ends location-selector div
        html = html + "</div>";
        //ends search-sidebar div
        html = html + "</div>";

        //gets form input through post
        String country_drop = context.formParam("state_drop");
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        
        //if no country is selected, no output is displayed on the page
        if(country_drop == null){

            html = html + "";
        }
        //creates output on page with data
        else {

            ArrayList<String> similarDeathRateState = jdbc.getSimilarDeathRateAusState(country_drop, startDate, endDate);
            ArrayList<String> similarHighestDeathsState = jdbc.similarHighestDeathsAusState(country_drop, startDate, endDate);
            ArrayList<String> similarTotalDeathsState = jdbc.similarTotalDeathsAusState(country_drop, startDate, endDate);
            ArrayList<String> similarTotalCasesState = jdbc.similarTotalCasesAusState(country_drop, startDate, endDate);


                html = html + "<div id='results-flex-container'>";

                //this provides an explaination at the top of the results div as to what the data below represents
                html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Similar Australian States to " + country_drop + "</h2>";
                    html = html + "<h5>The below results display COVID-19 stats similar to those of " + country_drop
                                    + "</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    //similar death rates to selected country within time period
                    html = html + "<p style='font-weight:bolder'>3 Most similar mortality rates: </p>";
                    
                    for (int i=0; i < similarDeathRateState.size(); i=i+2){

                        float deathRate = Float.parseFloat(similarDeathRateState.get(i+1));
                        double deathRate2d = Math.round(deathRate*100.0)/100.0;
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarDeathRateState.get(i) + ":</p>";
                        html = html + deathRate2d;
                        html = html + "%<br>";
                    }



                html = html + "</div>";

                html = html + "<div id='div2'>";
                    //closest highest death day
                    html = html + "<p style='font-weight:bolder'>3 Most similar highest death day: </p>";
                    
                    for (int i=0; i < similarHighestDeathsState.size(); i=i+3){

                        String maxNoDeaths = similarHighestDeathsState.get(i+1);
                        String maxDeathDate = similarHighestDeathsState.get(i+2);
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarHighestDeathsState.get(i) + ":</p>";
                        html = html + maxNoDeaths + " deaths <br>on " + maxDeathDate;
                        html = html + "<br>";
                    }




                html = html + "</div>";

                html = html + "<div id='div3'>";
                    //similar tptal deaths
                    html = html + "<p style='font-weight:bolder'>3 Most similar total deaths: </p>";
                    
                    for (int i=0; i < similarTotalDeathsState.size(); i=i+2) {
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalDeathsState.get(i) + ":</p>";
                        html = html + similarTotalDeathsState.get(i+1) + " deaths";
                    }  
                        
                    
                    html = html + "</div>";

                html = html + "<div id='div4'>";
                //similar total cases
                html = html + "<p style='font-weight:bolder'>3 Most similar total cases: </p>";
                
                for (int i=0; i < similarTotalCasesState.size(); i=i+2) {
                    html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalCasesState.get(i) + ":</p>";
                    html = html + similarTotalCasesState.get(i+1) + " cases";
                }  
                    
                
                html = html + "</div>";

                ArrayList<String> similarDeathRateUSState = jdbc.getSimilarDeathRateUSState(country_drop, startDate, endDate);
                ArrayList<String> similarHighestDeathsUSState = jdbc.similarHighestDeathsUSState(country_drop, startDate, endDate);
                ArrayList<String> similarTotalDeathsUSState = jdbc.similarTotalDeathsUSState(country_drop, startDate, endDate);
                ArrayList<String> similarTotalCasesUSState = jdbc.similarTotalCasesUSState(country_drop, startDate, endDate);

        html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Similar US States to " + country_drop + "</h2>";
                    html = html + "<h5>The below results display COVID-19 stats similar to those of " + country_drop
                                    + "</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    //similar death rates to selected country within time period
                    html = html + "<p style='font-weight:bolder'>3 Most similar mortality rates: </p>";
                    for (int i=0; i < similarDeathRateUSState.size(); i=i+2){

                        float deathRate = Float.parseFloat(similarDeathRateUSState.get(i+1));
                        double deathRate2d = Math.round(deathRate*100.0)/100.0;
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarDeathRateUSState.get(i) + ":</p>";
                        html = html + deathRate2d;
                        html = html + "%<br>";
                    }



                html = html + "</div>";

                html = html + "<div id='div2'>";
                    //closest highest death day
                    html = html + "<p style='font-weight:bolder'>3 Most similar highest death day: </p>";
                    
                    for (int i=0; i < similarHighestDeathsUSState.size(); i=i+3){

                        String maxNoDeaths = similarHighestDeathsUSState.get(i+1);
                        String maxDeathDate = similarHighestDeathsUSState.get(i+2);
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarHighestDeathsUSState.get(i) + ":</p>";
                        html = html + maxNoDeaths + " deaths <br>on " + maxDeathDate;
                        html = html + "<br>";
                    }




                html = html + "</div>";

                html = html + "<div id='div3'>";
                    //similar deaths per million
                    html = html + "<p style='font-weight:bolder'>3 Most similar total deaths: </p>";
                    
                    for (int i=0; i < similarTotalDeathsUSState.size(); i=i+2) {
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalDeathsUSState.get(i) + ":</p>";
                        html = html + similarTotalDeathsUSState.get(i+1) + " deaths";
                    }  
                        
                    
                html = html + "</div>";

                html = html + "<div id='div4'>";
                //similar total cases
                html = html + "<p style='font-weight:bolder'>3 Most similar total cases: </p>";
                
                for (int i=0; i < similarTotalCasesUSState.size(); i=i+2) {
                    html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalCasesUSState.get(i) + ":</p>";
                    html = html + similarTotalCasesUSState.get(i+1) + " cases";
                }  
                    
                
                html = html + "</div>";

                ArrayList<String> similarDeathRateCountryState = jdbc.getSimilarDeathRateCountryState(country_drop, startDate, endDate);
                ArrayList<String> similarHighestDeathsCountryState = jdbc.similarHighestDeathsCountryState(country_drop, startDate, endDate);
                ArrayList<String> similarTotalDeathsCountryState = jdbc.similarTotalDeathsCountryState(country_drop, startDate, endDate);
                ArrayList<String> similarTotalCasesCountryState = jdbc.similarTotalCasesCountryState(country_drop, startDate, endDate);

                    html = html + "<div id='divInfo' style='width:80%; border:none; background-color:transparent;'>";
                    html = html + "<h2>Similar Countries to " + country_drop + "</h2>";
                    html = html + "<h5>The below results display COVID-19 stats similar to those of " + country_drop
                                    + "</h5>";
                html = html + "</div>";

                html = html + "<div id='div1'>";
                    //similar death rates to selected country within time period
                    html = html + "<p style='font-weight:bolder'>3 Most similar mortality rates: </p>";
                    for (int i=0; i < similarDeathRateCountryState.size(); i=i+2){

                        float deathRate = Float.parseFloat(similarDeathRateCountryState.get(i+1));
                        double deathRate2d = Math.round(deathRate*100.0)/100.0;
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarDeathRateCountryState.get(i) + ":</p>";
                        html = html + deathRate2d;
                        html = html + "%<br>";
                    }



                html = html + "</div>";

                html = html + "<div id='div2'>";
                    //closest highest death day
                    html = html + "<p style='font-weight:bolder'>3 Most similar highest death day: </p>";
                    
                    for (int i=0; i < similarHighestDeathsCountryState.size(); i=i+3){

                        String maxNoDeaths = similarHighestDeathsCountryState.get(i+1);
                        String maxDeathDate = similarHighestDeathsCountryState.get(i+2);
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarHighestDeathsCountryState.get(i) + ":</p>";
                        html = html + maxNoDeaths + " deaths <br>on " + maxDeathDate;
                        html = html + "<br>";
                    }




                html = html + "</div>";

                html = html + "<div id='div3'>";
                    //similar deaths per million
                    html = html + "<p style='font-weight:bolder'>3 Most similar total deaths: </p>";
                    
                    for (int i=0; i < similarTotalDeathsCountryState.size(); i=i+2) {
                        html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalDeathsCountryState.get(i) + ":</p>";
                        html = html + similarTotalDeathsCountryState.get(i+1) + " deaths";
                    }  
                        
                    
                    html = html + "</div>";

                html = html + "<div id='div4'>";
                //similar total cases
                html = html + "<p style='font-weight:bolder'>3 Most similar total cases: </p>";
                
                for (int i=0; i < similarTotalCasesCountryState.size(); i=i+2) {
                    html = html + "<p style='font-weight:bold; margin:5px;'>" + similarTotalCasesCountryState.get(i) + ":</p>";
                    html = html + similarTotalCasesCountryState.get(i+1) + " cases";
                }  
                    
                
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