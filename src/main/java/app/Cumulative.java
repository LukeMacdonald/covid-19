package app;



import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;


public class Cumulative implements Handler {

    public static final String URL = "/cumulative_report.html";

    @Override
    public void handle(Context context) throws Exception {
        String html = "<html>";
        html = html + "<head>";
        html = html +"<title>Cumulative Report</title>";

        html = html + "<link rel='stylesheet' type='text/css' href='page5.css'/>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";

        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";
        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>";
        html = html + "<link href='https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css' rel='stylesheet' />";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js'></script>";

        html = html + "<script>"; 
        html = html + "$(document).ready(function(){";
        html = html + "$('#country_drop').select2();";
        html = html + "});";
        html = html + "</script>";
        html = html + "</head>";
       
        html = html + "<body>";
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html'style = 'background-color:#3189af'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html' >Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px'><a href = '/'>Home</a> > <a href = '/cumulative_report.html'>Cumulative Report</a></p>"; 
        html = html + "<h1>THE CUMULATIVE REPORT OF COVID-19</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides culmutive reports for each country about,"
                    + "the total case and death numbers.</h5>";
        html = html + "</div>";
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/cumulative_report.html' style = 'background-color: rgb(241, 241, 241)'>Cumulative Report per Country</a>";
        html = html + " <a href='/similarcountries.html'>Similar Countries</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
        
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> countries = jdbc.getCountries();
    
        html = html + "<div class = 'location_selector'>";
        html = html + "<form action='/cumulative_report.html' method='post'>";
        html = html + "<label for='country_drop' id = 'label1'>Select Country:</label><br>";
        html = html + "<select id='country_drop' name='country_drop' placeholder='Pick a state...'>";
        html = html + "   <option value = 'select'>Select a Country...</option>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + " </select>";
        html = html + "<div class='form-group row'>";
        html = html + " <input class='form-control' type = 'radio' id ='all_time' name='date' value = 'allday' >";
        html = html + " <label for = 'all_time'>All Time</label>";
        html = html + "</div>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input class='form-control' type ='radio' id ='last_days' name='date' value = 'lastday' >";
        html = html + "  <label for = 'last_daysx'>Last x Days:</label>";
        html = html + "  <input class='form-control' type='number' id ='last_daysx' name='last_daysx' min='0' max = '456' >";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";

        String country_drop = context.formParam("country_drop");
        String selection = context.formParam("date");
        String xDays = context.formParam("last_daysx");

        if(selection == null && (country_drop == null || country_drop.equals("select"))){
            html = html + "<p style = 'color:red;'>Select Country and Date</p>";
        }
        else if((country_drop == null || country_drop.equals("select"))){
            html = html + "<p style = 'color:red;'>Select A Country</p>";
        }
        else if(selection == null){
            html = html + "<p style = 'color:red;'>Select A Date Range</p>";
        }
        html = html + "</div>";

        html = html + getStats(country_drop,selection,xDays);
        
        html = html + "</body>" + "</html>";
        context.html(html);
    }
    public String getStats(String country,String selection,String numDays){
        JDBCConnection jdbc = new JDBCConnection();
        String html = "";
        if(selection == null || (country == null || country.equals("select"))){
            html = html + "";
        }
        else if (!country.isBlank() && selection.equals("lastday")){
            int population = jdbc.population(country);
            int cases = jdbc.getXDays(country,numDays);
            int deaths = jdbc.getXDeaths(country,numDays);
            double ratio1 = (deaths / cases) * 100.0;
            double ratio2 = ((double)deaths / population) * 100;
            double ratio3 = ((double)cases / population) * 100;
            html = html + "<h2 style = 'position: relative;left:2%;'>Cumulative Report per Country</h2>";
            html = html + "<h3 style = 'position:relative; left:4%'>" + country +"</h3>";
            html = html + "<h5 style = 'position:relative; left:4%'>Last " + numDays + " Days</h5>";
            html = html + "<div class = 'container1'>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'population'>";
            html = html + "<h6>Population</h6>";
            html = html + "<h4>" + population + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4' id = 'infection'>";
            html = html + "<h6>Total Cases</h6>";
            html = html + "<h4>" + cases + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'dates' >";
            html = html + "<h6>Total Deaths</h6>";
            html = html + "<h4>" + deaths + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio1' >";
            html = html + "<h6>Total Deaths</h6>";
            html = html + "<h4>" + ratio1 + "%</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio2' >";
            html = html + "<h6>Total Deaths</h6>";
            html = html + "<h4>" + String.format("%2.2f",ratio2)+ "%</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio3' >";
            html = html + "<h6>Total Deaths</h6>";
            html = html + "<h4>" + String.format("%2.2f",ratio3) + "%</h4>";
            html = html + "</div>";
            html = html + "</div>";

            
        } else if (!country.isBlank() && selection.equals("allday")){
            int population = jdbc.population(country);
            int cases = jdbc.getSumCases(country,"2020-01-22","2021-04-22");
            int deaths = jdbc.getSumDeaths(country,"2020-01-22","2021-04-22");
            double ratio1 = (deaths / cases) * 100.0;
            double ratio2 = ((double)deaths / population) * 100;
            double ratio3 = ((double)cases / population) * 100;
      
            html = html + "<h2 style = 'position: relative;left:2%;'>Cumulative Report per Country</h2>";

            html = html + "<h3 style = 'position:relative; left:4%'>" + country +  "<h3><h5 style = 'position:relative; left:4%'>22-01-2020 to 22-04-2021</h5>";
            html = html + "<div class = 'container1'>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'population'>";
            html = html + "<h6>Population</h6>";
            html = html + "<h4>" + population + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4' id = 'infection'>";
            html = html + "<h6>Total Cases</h6>";
            html = html + "<h4>" + cases + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'dates' >";
            html = html + "<h6>Total Deaths</h6>";
            html = html + "<h4>" + deaths + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio1' >";
            html = html + "<h6>Ratio of Deaths to Infection</h6>";
            html = html + "<h4>" + ratio1 + "%</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio2' >";
            html = html + "<h6>Ratio of Deaths to Total Population</h6>";
            html = html + "<h4>" + String.format("%2.2f",ratio2)+ "%</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'ratio3' >";
            html = html + "<h6>Ratio of Infections to Total Popultion</h6>";
            html = html + "<h4>" + String.format("%2.2f",ratio3) + "%</h4>";
            html = html + "</div>";
            html = html + "</div>";
        }
        return html;
    }

}
        


    

