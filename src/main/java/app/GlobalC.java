package app;
import java.util.ArrayList;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class GlobalC implements Handler{
    public static final String URL = "/cumalative_global.html";
    JDBCConnection jdbc = new JDBCConnection();

    @Override
    public void handle(Context context) throws Exception {
        ArrayList<String> countries = jdbc.getCountries();
        String html = "<html>";

        
        html = html + "<head>";
        html = html +"<title>Cases</title>";
        html = html + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";
        html = html + "<link rel='stylesheet' type='text/css' href='page5.css'/>";
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
        html = html + " <a href='/cumalative_global.html' style = 'background-color: rgb(241, 241, 241)'>Global Cumulative Report</a>";
        html = html + "<a href = '/cumulative_report.html'>Country Cumulative Report</a>";
        html = html + "<a href='/worldmap1.html'>Global Map</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";
       
        
        //Form
        html = html + "<div class = 'location_selector'>";
        html = html + "  <form action='/cumalative_global.html' method='post'>";
        html = html + "  <label for = 'date'>Enter Date Range</label>"; 
        html = html + "<div class='form-group row'>";
        html = html + "  <input  type = 'radio' id ='all_time' name='date' value = 'allday' required>";
        html = html + "  <label for = 'all_time'>All Time</label>";
        html = html + "</div>";
        html = html + "<div class='form-group row'>";
        html = html + "  <input  type ='radio' id ='last_days' name='date' value = 'lastday' >";
        html = html + "  <label for = 'last_daysx'>Last <span id = 'day'></span> Days:</label>";
        html = html + "  <input  type = 'range' id ='last_daysx' name='last_daysx' min='0' max = '456' value = '0' style = 'width: 200px'>";
        html = html + "</div>";
        html = html + "  <label for='sort' id = 'sortLabel'>Sort By:</label>";
        html = html + " <select id='sort' name='sort' style = 'width:200px' >";
        html = html + "   <option value = 'most'>Most Affected</option>";
        html = html + "   <option value = 'least'>Least Affected</option>";
        html = html + "  </select>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";
        html = html + "</div>";
 
        String selection = context.formParam("date");
        String numDays = context.formParam("last_daysx");
        String sort = context.formParam("sort");
        

        
        //Creates Table
        if(selection == null){
            html = html + "";
        }
            else if (selection.equals("allday") && sort.equals("most")){

                ArrayList<String>MostAff = jdbc.getMostAffected();
                html = html + "<h2 style = 'position: relative;left:2%;'>Global Cumulative Results</h2>";
                html = html + "<div class = 'container1'>";
                html = html + allDay();
                html = html + "<p style = 'font-size: 15px; left: 2%'>The table below shows the total number of COVID-19 cases and deaths for each country per 1 million people. Death rate and infection rate have also been calculated. This Data was collected from 22/01/2020 to 22/04/2021. Data that displays NaN% means that there have been no reported infections during that time period. <b> Click on table headers to sort data by that column<b></p>";
                html = html + "<div class = 'table1'>";
                html = html + "<table class='sortable, table table-bordered'>";
                html = html + "<tr>";
                html = html + "<th>Country</th>";
                html = html + "<th>Population</th>";
                html = html + "<th>Total Cases</th>";
                html = html + "<th>Total Deaths</th>";
                html = html + "<th>Death Rate</th>";
                html = html + "<th>Infection Rate</th>";
                for(int i = 0; i < countries.size();i++){
                    String country = MostAff.get(i);
                    int cases = jdbc.getSumCases(country,"2020-01-22","2021-04-22");
                    int deaths = jdbc.getSumDeaths(country, "2020-01-22","2021-04-22");
                    int population = jdbc.population(country);
                    double casesPerMil = ((double)cases / population)*1000000;
                    double dPerMil = ((double)deaths / population)*1000000;
                    double ratio2 = ((double)deaths / cases) * 100;
                    double ratio3 = ((double)cases / population) * 100;
                    html = html + "</tr>";
                    html = html + "   <td>" + country + "</td>";
                    html = html + "   <td>" + population + "</td>";
                    html = html + "   <td>" + String.format("%2.2f",casesPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",dPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio2)+ "%</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio3)+ "%</td>";
                    html = html + "</tr>";
                    html = html + "</tr>";
                    }
                    html = html + "</table>";
                }
                else if (selection.equals("allday") && sort.equals("least")){
                    ArrayList<String>LeastAff = jdbc.getLeastAffected();
                    html = html + "<h2 style = 'position: relative;left:2%;'>Global Cumulative Results</h2>";
                    html = html + "<div class = 'container1'>";
                    html = html + allDay();
                    html = html + "<p style = 'font-size: 15px; left: 2%'>The table below shows the total number of COVID-19 cases and deaths for each country per 1 million people. Death rate and infection rate have also been calculated. This Data was collected from 22/01/2020 to 22/04/2021. Data that displays NaN% means that there have been no reported infections during that time period. <b> Click on table headers to sort data by that column<b></p>";
                    html = html + "<div class = 'table1'>";
                    html = html + "<table class='sortable, table table-bordered'>";
                    html = html + "<tr>";
                    html = html + "<th>Country</th>";
                    html = html + "<th>Population</th>";
                    html = html + "<th>Total Cases</th>";
                    html = html + "<th>Total Deaths</th>";
                    html = html + "<th>Death Rate</th>";
                    html = html + "<th>Infection Rate</th>";
                    for(int i = 0; i < countries.size();i++){
                    String country = LeastAff.get(i);
                    int cases = jdbc.getSumCases(country,"2020-01-22","2021-04-22");
                    int deaths = jdbc.getSumDeaths(country, "2020-01-22","2021-04-22");
                    int population = jdbc.population(country);
                    double casesPerMil = ((double)cases / population)*1000000;
                    double dPerMil = ((double)deaths / population)*1000000;
                    double ratio2 = ((double)deaths / cases) * 100;
                    double ratio3 = ((double)cases / population) * 100;
                    html = html + "</tr>";
                    html = html + "<tr class = 'item'>";
                    html = html + "   <td>" + country + "</td>";
                    html = html + "   <td>" + population + "</td>";
                    html = html + "   <td>" + String.format("%2.2f",casesPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",dPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio2)+ "%</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio3)+ "%</td>";
                    html = html + "</tr>";
                    html = html + "</tr>";
                    }
                    html = html + "</table>";
                    html = html + "</div>";   
                    html = html + "</div>"; 
                }
                else if (selection.equals("lastday") && sort.equals("most")){
                    ArrayList<String>MostAff = jdbc.getXMostAffected(numDays);
                    html = html + "<h2 style = 'position: relative;left:2%;'>Global Cumulative Results</h2>";
                    html = html + "<div class = 'container1'>";
                    html = html + xDay(numDays);
                
                    html = html + "<p style = 'font-size: 15px; left: 2%'>The table below shows the total number of COVID-19 cases and deaths for each country per 1 million people. Death rate and infection rate have also been calculated. This Data was collected from 22/01/2020 to 22/04/2021. Data that displays NaN% means that there have been no reported infections during that time period. <b> Click on table headers to sort data by that column<b></p>";
                    html = html + "<div class = 'table1'>";
                    html = html + "<table class='sortable, table table-bordered'>";
                    html = html + "<tr>";
                    html = html + "<th>Country</th>";
                    html = html + "<th>Population</th>";
                    html = html + "<th>Total Cases</th>";
                    html = html + "<th>Total Deaths</th>";
                    html = html + "<th>Death Rate</th>";
                    html = html + "<th>Infection Rate</th>";
                    for(int i = 0; i < MostAff .size();i++){
                    String country = MostAff .get(i);
                    int population = jdbc.population(country);
                    int cases = jdbc.getXDays(country,numDays);
                    double casesPerMil = ((double)cases / population)*1000000;
                    int deaths = jdbc.getXDeaths(country,numDays);
                    double dPerMil = ((double)deaths / population)*1000000;
                    double ratio2 = ((double)deaths / cases) * 100;
                    double ratio3 = ((double)cases / population) * 100;
                    html = html + "</tr>";
                    html = html + "<tr class = 'item'>";
                    html = html + "   <td>" + country + "</td>";
                    html = html + "   <td>" + population + "</td>";
                    html = html + "   <td>" + String.format("%2.2f",casesPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",dPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio2)+ "%</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio3)+ "%</td>";
                    html = html + "</tr>";
                    }
                    html = html + "</table>";
                    html = html + "</div>"; 
                    html = html + "</div>";      
                }
                else if (selection.equals("lastday") && sort.equals("least")){
                    ArrayList<String>LeastAff = jdbc.getXLeastAffected(numDays);
                    html = html + "<h2 style = 'position: relative;left:2%;'>Global Cumulative Results</h2>";
                    html = html + "<div class = 'container1'>";
                    html = html + xDay(numDays);
                    html = html + "<p style = 'font-size: 15px; left: 2%'>The table below shows the total number of COVID-19 cases and deaths for each country per 1 million people. Death rate and infection rate have also been calculated. This Data was collected from 22/01/2020 to 22/04/2021. Data that displays NaN% means that there have been no reported infections during that time period. <b> Click on table headers to sort data by that column<b></p>";
                    html = html + "<div class = 'table1'>";
                    html = html + "<table class='sortable, table table-bordered'>";
                    html = html + "<tr>";
                    html = html + "<th>Country</th>";
                    html = html + "<th>Population</th>";
                    html = html + "<th>Total Cases</th>";
                    html = html + "<th>Total Deaths</th>";
                    html = html + "<th>Death Rate</th>";
                    html = html + "<th>Infection Rate</th>";
             
                    for(int i = 0; i < LeastAff .size();i++){
                    String country = LeastAff .get(i);
                    int population = jdbc.population(country);
                    int cases = jdbc.getXDays(country,numDays);
                    double casesPerMil = ((double)cases / population)*1000000;
                    int deaths = jdbc.getXDeaths(country,numDays);
                    double dPerMil = ((double)deaths / population)*1000000;
                    double ratio2 = ((double)deaths / cases) * 100;
                    double ratio3 = ((double)cases / population) * 100;
                    html = html + "</tr>";
                    html = html + "<tr class = 'item'>";
                    html = html + "   <td>" + country + "</td>";
                    html = html + "   <td>" + population + "</td>";
                    html = html + "   <td>" + String.format("%2.2f",casesPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",dPerMil)+ "</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio2)+ "%</td>";
                    html = html + "   <td>" + String.format("%2.2f",ratio3)+ "%</td>";
                    html = html + "</tr>";
                    }
                    html = html + "</table>"; 
                }
                html = html + "</div>";   
                html = html + "</div>";
        html = html + "</body>";
             //Days slider
             html = html + "<script>";
        
             //Days slider
             html = html + "var slider1 = document.getElementById('last_daysx');";
             html = html + "var output1 = document.getElementById('day');";
             html = html + "output1.innerHTML = slider1.value;";
             html = html + "slider1.oninput = function() {";
             html = html + " output1.innerHTML = this.value;";
             html = html + "};";
             html = html + "</script>";  
        html = html + "</html>";
        context.html(html);
    }
    public String allDay(){
        String html = "";
        long population = jdbc.globalPopulation();
        int cases = jdbc.getGlobalCases("2020-01-22", "2021-04-22");
        int deaths = jdbc.getGlobalDeaths("2020-01-22","2021-04-22");
        double ratio1 = (deaths / cases) * 100.0;
        double ratio2 = ((double)deaths / population) * 100;
        double ratio3 = ((double)cases / population) * 100;
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
    public String xDay(String day){
        String html = "";

        long population = jdbc.globalPopulation();
        int cases = jdbc.getGlobalXCases(day);
        int deaths = jdbc.getGlobalXDeaths(day);
        double ratio1 = (deaths / cases) * 100.0;
        double ratio2 = ((double)deaths / population) * 100;
        double ratio3 = ((double)cases / population) * 100;
        html = html + "<h5 style = 'position:relative; left:4%'>Last " + day + " Days</h5>";
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
}
    


    

