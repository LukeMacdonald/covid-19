package app;
import java.util.ArrayList;
import io.javalin.http.Context;
import io.javalin.http.Handler;


public class Cumulative implements Handler {
    public static final String URL = "/cumulative_report.html";
    JDBCConnection jdbc = new JDBCConnection();
    ArrayList<String> countries = jdbc.getCountries();
    @Override
    public void handle(Context context) throws Exception {
        String html = "<html>";
        html = html + "<head>";
        html = html +"<title>Cumulative Report</title>";
        html = html + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";
        html = html + "<link rel='stylesheet' type='text/css' href='page5.css'/>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";
        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>";
        html = html + "<script src='https://www.kryogenix.org/code/browser/sorttable/sorttable.js'></script>";
        html = html + "<link href='https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css' rel='stylesheet' />";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js'></script>";

        html = html + "<script>"; 
        
        //Search Bar
        html = html + "$(document).ready(function(){";
        html = html + "$('#country_drop').select2();";
        html = html + "});";   
        html = html + "</script>";

        html = html + "</head>";
        html = html + "<body>";
        
        //Navbar
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumalative_global.html'style = 'background-color:#3189af'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html' >Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        
        //Title
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px'><a href = '/'>Home</a> > <a href = '/cumulative_report.html'>Cumulative Report</a></p>"; 
        html = html + "<h1>THE CUMULATIVE REPORT OF COVID-19</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides culmutive reports for each country about,"
                    + "the total case and death numbers.</h5>";
        html = html + "</div>";
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/cumalative_global.html' >Global Cumulative Report</a>";
        html = html + "<a href = '/cumulative_report.html' style = 'background-color: rgb(241, 241, 241)'>Country Cumulative Report</a>";
        html = html + "<a href='/worldmap1.html'>Global Map</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
        
        //Form
        html = html + "<div class = 'location_selector'>";
        html = html + "  <form action='/cumulative_report.html' method='post'>";
        html = html + "  <label for='country_drop' id = 'label1'>Select Country:</label><br>";
        html = html + "  <select id='country_drop' name='country_drop' style = 'width:200px' requried>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + "  </select>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input  type = 'radio' id ='all_time' name='date' value = 'allday' required>";
        html = html + "  <label for = 'all_time'>All Time</label>";
        html = html + "</div>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input  type ='radio' id ='last_days' name='date' value = 'lastday' >";
        html = html + "  <label for = 'last_daysx'>Last <span id = 'day'></span> Days:</label>";
        html = html + "  <input  type = 'range' id ='last_daysx' name='last_daysx' min='0' max = '456' value = '0' style = 'width: 200px'>";
        html = html + "</div>";
        html = html + "<button class='accordion' type = 'button'>Compare To:</button>";
        html = html + "<div class='panel'>";
        html = html + "<div class='form-group row'>";
        html = html + " <input type = 'radio' id ='similar_clim' name='select' value = 'climate' onclick = 'showSort()'>";
        html = html + " <label for = 'similar_clim'>Countries With Similar Climate</label>";
        html = html + "</div>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input  type ='radio' id ='distance' name='select' value = 'distance' onclick = 'showSort()'>";
        html = html + "  <label for = 'distance'> Countries Within: <span id = 'dis'></span> km</label>";
        html = html + "  <input  type='range' id ='distancex' name='distancex' min='0' max ='25000' value = '0' style = 'width: 200px; margin-left:20px'>";
        html = html + "</div>";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";
        html = html + "</div>";

        //Info from form
        String country = context.formParam("country_drop");
        String selection = context.formParam("date");
        String numDays = context.formParam("last_daysx");
        String selection2 = context.formParam("select");
        String distance = context.formParam("distancex");
        
        //Displaying COVID-19 Data
        if(selection == null || (country == null || country.equals("select"))){
            html = html + "";
        }
        else if (!country.isBlank() && selection.equals("lastday") && selection2 == null){
            html = html + xDays(country, numDays);
        } else if (!country.isBlank() && selection.equals("allday") &&selection2 == null){
            html = html + allDay(country);
        }
        else if (!country.isBlank() && selection.equals("lastday")&& selection2.equals("climate")){
            html = html + xDays(country, numDays);
            html = html + SimilarClimate(country);
        } else if (!country.isBlank() && selection.equals("allday")&&selection2.equals("climate")){
            html = html + allDay(country);
            html = html + SimilarClimate(country);
        }
        else if (!country.isBlank() && selection.equals("lastday") && selection2.equals("distance")){
            html = html + xDays(country, numDays);
            html = html + CountryDistance(country, distance);

        } else if (!country.isBlank() && selection.equals("allday") && selection2.equals("distance")){
            html = html + allDay(country);
            html = html + CountryDistance(country, distance);
        }
        html = html + "</body>";
        
        html = html + "<script>";
        
        //Days slider
        html = html + "var slider = document.getElementById('distancex');";
        html = html + "var output = document.getElementById('dis');";
        html = html + "output.innerHTML = slider.value;";
        html = html + "var slider1 = document.getElementById('last_daysx');";
        html = html + "var output1 = document.getElementById('day');";
        html = html + "output1.innerHTML = slider1.value;";
        
        //Distance slider
        html = html + "slider.oninput = function() {";
        html = html + " output.innerHTML = this.value;";
        html = html + "};";
        html = html + "slider1.oninput = function() {";
        html = html + " output1.innerHTML = this.value;";
        html = html + "};";
      

        html = html + "var acc = document.getElementsByClassName('accordion');";
        html = html + "var i;";
        
        html = html + "for (i = 0; i < acc.length; i++) {";
        html = html + "acc[i].addEventListener('click', function() {";
        html = html + "/* Toggle between adding and removing the 'active' class,";
        html = html + "to highlight the button that controls the panel */";
        html = html + "this.classList.toggle('active');";
        
        html = html + "var panel = this.nextElementSibling;";
        html = html + "if (panel.style.display === 'block') {";
        html = html + "panel.style.display = 'none';";
        html = html + "} else {";
        html = html + "panel.style.display = 'block';";
        html = html + "}";
        html = html + "});";
        html = html + "}";
        
        html = html + "</script>";  
        html = html + "</html>";
        context.html(html);
    }
    public String SimilarClimate(String country){
        String html = "";
        ArrayList<String>SimilarClimate = jdbc.getSimilarClimate(country);
        html = html + "<h2 style = 'position: relative;left:2%;'>Comparing Cumalive Report With Other Countries</h2>";
        html = html + "<div class = 'container1'>";
        html = html + "<h4>Culmative Report of Countries with similar climates of " + country + "</h4>";
        html = html + "<p style = 'font-size: 15px'>This table shows the total number of COVID-19 cases and deaths in the top 5 countries with simlar climates to " + country + ". The Data was collected from 22/01/2020 to 22/04/2021.<b> Click on table headers to sort data by that column<b></p>";
        html = html + "<div class = 'table1'>";
        html = html + "<table class = 'table table-bordered,sortable'>";
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
        return html;
        }
        public String CountryDistance(String country, String distance){
            String html = "";
            int dis = Integer.parseInt(distance);
            ArrayList<String>SimilarCountries = jdbc.getSimilarCountries(country,dis);
            ArrayList<Double>distances = jdbc.getSimCountDis(country, dis);
            html = html + "<h2 style = 'position: relative;left:2%;'>Comparing Cumalive Report With Other Countries</h2>";
            html = html + "<div class = 'container1'>";
            html = html + "<h4>Culmative Report of Countries within " + dis + " km of " + country + "</h4>";
            html = html + "<p style = 'font-size: 15px'>This table shows the total number of COVID-19 cases and deaths in"
                            + " countries within " + dis + " km of " + country + ". The Data was collected from 22/01/2020" 
                            +" to 22/04/2021. Similar Climates have been determined by comparing countries with the similar latitudes. <b> Click on table headers to sort data by that column<b></p> ";
            html = html + "<div class = 'table1'>";
            html = html + "<table class = 'table table-bordered,sortable'>";
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
            return html;
        }
        public String allDay(String country){
            String html = "";
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
            return html;
        }
        public String xDays(String country, String numDays){
            String html = "";
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
            return html;
        }
    }
        


    

