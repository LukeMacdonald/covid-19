package app;

import io.javalin.http.Handler;

import java.util.ArrayList;

import io.javalin.http.Context;

public class GlobalInfect implements Handler {
    public static final String URL = "/infection_global.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> countries = jdbc.getCountries();
        String html = "<html>";

        
        html = html + "<head>";
        html = html +"<title>Cases</title>";
        html = html + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";
        html = html + "<link rel='stylesheet' type='text/css' href='common.css'/>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";
        html = html + "<script src='https://www.kryogenix.org/code/browser/sorttable/sorttable.js'></script>";
   
        html = html + "<script>";
        
           //Function to set the end dates min value to the start dates input
           html = html + "function updatedate() {";
           html = html + " var firstdate = document.getElementById('startDate').value;";
           html = html + " document.getElementById('endDate').value = '';";
           html = html + " document.getElementById('endDate').setAttribute('min',firstdate);";
           html = html + "}";
        html = html + "</script>";
        html = html + "</head>";
       
        html = html + "<body>";
        
        //Navbar
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html' style = 'background-color:#3189af'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumalative_global.html'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
        
        //Header
        
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px'><a href = '/'>Home</a> > <a href = '/infection_global.html'>Infection Rates</a></p>"; 
        html = html + "<h1>TRACKING INFECTION RATE OF COVID-19</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides updates about the current situation,"
                           + "latest case numbers and related information.</h5>";
        html = html + "</div>";
        //Second Narbar
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/infection_global.html' style = 'background-color: rgb(241, 241, 241)'>Global Infections</a>";
        html = html + "<a href = '/infection_country.html'>Country Infections</a>";
        html = html + "<a href='/infection_state.html'>State/Province Infections</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
        
        //Form
        html = html + "<form class = action='/infection_global.html' method='post' >";
        html = html + " <div style = 'padding-top:20px' id = 'date_selector'>";
        html = html + " <label for = 'startDate'>Select Start Date:</label>";
        html = html + " <input class='form-control' type='date' id ='startDate' name='startDate' onchange= 'updatedate()' min = '2020-01-22' max = '2021-04-22' value = '2020-01-22'>";
        html = html + "  <label for = 'endDate'>Select End Date:</label>";
        html = html + "  <input class='form-control' id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'>";
        html = html + " <input class='form-control' type = 'radio' id ='all_time' name='date' value = 'allday' >";
        html = html + " <label for = 'all_time' style = 'postion:relative; margin-top:8px;'>All Time</label>";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";
 
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        
        //Creates Table
        if(startDate == null ){
            html = html + "";
        }
        else{
            int caseGlobal = jdbc.getGlobalCases(startDate,endDate);
            String dateGlobal = jdbc.getGlobalDate(startDate,endDate);
            long population = jdbc.globalPopulation();
            html = html + "<h2 style = 'position: relative;left:2%;'>Global Infection Rates</h2>";
        
            html = html + "<div class = 'container1'>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'population'>";
            html = html + "<h6>Population</h6>";
            html = html + "<h4>" + population + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4' id = 'infection'>";
            html = html + "<h6>Total Cases</h6>";
            html = html + "<h4>" + caseGlobal + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'dates' >";
            html = html + "<h6>Date of Highest Case Increase</h6>";
            html = html + "<h4>" + dateGlobal + "</h4>";
            html = html + "</div>";
            html = html + "<h4>Infection Rates of Each Country</h4>";
            html = html + "<p style = 'font-size: 15px; left: 2%'>The table below shows the total number of COVID-19 cases and the date when there was the greatest increase in cases in each country of the world during the specifed date range. The table can be sorted by either country name or by the number of cases by clicking on the columns header. Sorting by number of shows which countries have been most and least affected by COVID-19. This Data was collected from 22/01/2020 to 22/04/2021.<b> Click on table headers to sort data by that column<b></p>";
            html = html + "<div class = 'table4'>";
            html = html + "<table class='sortable, table table-bordered'>";
            html = html + "<tr>";
            html = html + "<th>Country</th>";
            html = html + "<th>Total Cases</th>";
            html = html + "<th>Date of Highest Case Increase</th>";
            for(int i = 0; i < countries.size();i++){
            String country = countries.get(i);
            int sumCases = jdbc.getSumCases(country,"2020-01-22","2021-04-22");
            String date1 = jdbc.getHighestDate(country, startDate, endDate);


            html = html + "</tr>";
            html = html + "<tr class = 'item'>";
            html = html + "   <td>" + country + "</td>";
            html = html + "   <td>" + sumCases + "</td>";
            html = html + "   <td>" + date1 + "</td>";
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
    
