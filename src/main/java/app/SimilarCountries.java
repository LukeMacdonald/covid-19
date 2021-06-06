package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class SimilarCountries implements Handler {
    public static final String URL = "/similarcountries.html";
    
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
        html = html + " <a href='/'>Home</a>";
        html = html + "<a href = '/page2.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/page4.html'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html' style = 'background-color:#3189af;'>Cumulative Reports</a>";
        html = html + "<a href='/page6.html'>Country Comparasion</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px'><a href = '/'>Home</a> > <a href = '/cumulative_report.html'>Cumulative Report</a></p>"; 
        html = html + "<h1>THE CUMULATIVE REPORT OF COVID-19</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides culmutive reports for each country about,"
                           + "the total case and death numbers.</h5>";
        html = html + "</div>";
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/cumulative_report.html'>Cumulative Report per Country</a>";
        html = html + " <a href='/similarcountries.html' style = 'background-color: rgb(241, 241, 241)'>Similar Countries</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
        
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> countries = jdbc.getCountries();
    
        html = html + "<div class = 'location_selector'>";
        html = html + "<form action='/similarcountries.html' method='post'>";
        html = html + "<label for='country_drop' id = 'label1'>Select Country:</label><br>";
        html = html + "<select id='country_drop' name='country_drop' placeholder='Pick a state...'>";
        html = html + "   <option value = 'select'>Select a Country...</option>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + " </select>";
        html = html + "<div class='form-group row'>";
        html = html + " <input class='form-control' type = 'radio' id ='similar_clim' name='select' value = 'climate'>";
        html = html + " <label for = 'similar_clim'>Similar Climate</label>";
        html = html + "</div>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input class='form-control' type ='radio' id ='distance' name='select' value = 'distance'>";
        html = html + "  <label for = 'distance'> x Distance:</label>";
        html = html + "  <input class='form-control' type='number' id ='distancex' name='distancex' min='0'>";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button style = >";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";
        html = html + "</div>";

        String country_drop = context.formParam("country_drop");
        String selection = context.formParam("select");
        String distance = context.formParam("distancex");
        System.out.println(country_drop);
       
        if(selection == null || (country_drop == null || country_drop.equals("select"))){
            html = html + "";
        }  else if (!country_drop.isBlank() && selection.equals("climate")){
            ArrayList<String>SimilarClimate = jdbc.getSimilarClimate(country_drop);
            ArrayList<Double>distances = jdbc.getSimClimDis(country_drop);
            html = html + "<h2 style = 'position: relative;left:2%;'>Comparing Cumalive Report With Other Countries</h2>";
            html = html + "<div class = 'container1'>";
            html = html + "<h4>Culmative Report of Countries with similar climates of " + country_drop + "</h4>";
            html = html + "<p style = 'font-size: 15px'>This table shows the total number of COVID-19 cases and deaths in the top 5 countries with simlar climates to " + country_drop + ". The Data was collected from 22/01/2020 to 22/04/2021 .</p>";
            html = html + "<div class = 'table1'>";
            html = html + "<table class = 'table table-bordered'>";
            html = html + "<tr>";
            html = html + "</tr>";
            html = html + "<tr>";
            html = html + "<th>#</th>";
            html = html + "<th>Similar Country</th>";
            html = html + "<th>Population</th>";
            html = html + "<th>Total Cases</th>";
            html = html + "<th>Total Death</th>";
            html = html + "</tr>";
            for (int i = 0; i < SimilarClimate.size();i++){
                html = html + "<tr>";
                html = html + "<td style = 'font-size: 15px;'>" + (i + 1) + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + SimilarClimate.get(i) + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.population(SimilarClimate.get(i)) + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.getSumCases(SimilarClimate.get(i),"2020-01-22","2021-04-22") + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.getSumDeaths(SimilarClimate.get(i),"2020-01-22","2021-04-22") + "</td>";
                html = html + "</tr>";
            }
            
            html = html + "</table>";
            html = html + "</div>";
            html = html + "</div>";
        }
        else if (!country_drop.isBlank() && selection.equals("distance")){
            int dis = Integer.parseInt(distance);
            System.out.println(dis);
            ArrayList<String>SimilarCountries = jdbc.getSimilarCountries(country_drop,dis);
            ArrayList<Double>distances = jdbc.getSimCountDis(country_drop, dis);
            html = html + "<h2 style = 'position: relative;left:2%;'>Comparing Cumalive Report With Other Countries</h2>";
            html = html + "<div class = 'container1'>";
            html = html + "<h4>Culmative Report of Countries within " + dis + " km of " + country_drop + "</h4>";
            html = html + "<p style = 'font-size: 15px'>This table shows the total number of COVID-19 cases and deaths in"
                            + " countries within " + dis + " km of " + country_drop + ". The Data was collected from 22/01/2020" 
                            +" to 22/04/2021. Similar Climates have been determined by comparing countries with the similar latitudes</p>";
            html = html + "<div class = 'table1'>";
            html = html + "<table class = 'table table-bordered'>";
            html = html + "<tr>";
            html = html + "</tr>";
            html = html + "<tr>";
            html = html + "<th>Similar Country</th>";
            html = html + "<th>Population</th>";
            html = html + "<th>Total Cases</th>";
            html = html + "<th>Total Death</th>";
            html = html + "<th>Total Distance (km)</th>";
            html = html + "</tr>";
            for (int i = 0; i < SimilarCountries.size();i++){
                html = html + "<tr>";
                html = html + "<td style = 'font-size: 15px;'>" + SimilarCountries.get(i) + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.population(SimilarCountries.get(i)) + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.getSumCases(SimilarCountries.get(i),"2020-01-22","2021-04-22") + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + jdbc.getSumDeaths(SimilarCountries.get(i),"2020-01-22","2021-04-22") + "</td>";
                html = html + "<td style = 'font-size: 15px;'>" + String.format("%.2f", distances.get(i)) + "</td>";
                html = html + "</tr>";
            }
            
            html = html + "</table>";
            html = html + "</div>";
            html = html + "</div>";
        }

        html = html + "</body>" + "</html>";
        context.html(html);
    }
    
}
