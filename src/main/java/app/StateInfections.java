package app;

import io.javalin.http.Handler;
import java.util.ArrayList;
import io.javalin.http.Context;

public class StateInfections implements Handler {
    public static final String URL = "/infection_state.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> state = jdbc.getStates();
        String html = "<html>";

        html = html + "<head>";
        html = html +"<title>Cases</title>";
        html = html + "<link rel='stylesheet' type='text/css' href='common.css'/>";
        html = html + "<meta name='viewport' content='width=device-width, initial-scale=1'>";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";
        html = html + "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js'></script>";
        html = html + "<link href='https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css' rel='stylesheet' />";
        html = html + "<script src='https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js'></script>";
        
        html = html + "<script>";
        //Creates a searchable dropdown menu
        html = html + "$(document).ready(function(){";
        html = html + "$('#state_drop').select2();";
        html = html + "});";
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
        html = html + " <a href='/'>Home</a>";
        html = html + "<a href = '/page2.html'>Facts</a>";
        html = html + "<a href='/infection_global.html' style = 'background-color:#3189af;'>Infection Rates</a>";
        html = html + "<a href='/page4.html'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html'>Cumulative Reports</a>";
        html = html + "<a href='/page6.html'>Country Comparasion</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";
       
        //Header
        html = html + "<div class = 'title'>" + "<p style = 'font-size:12px;margin-top:-5px'><a href = '/'>Home</a> > <a href = '/infection_global.html'>Infection Rates</a></p>"; 
        html = html + "<h1>TRACKING INFECTION RATE OF COVID-19</h1>";
        html = html + "<h5 style = 'position: relative; left: 2%; width: 600px'>This page provides updates about the current situation,"
                           + "latest case numbers and related information.</h5>";
        html = html + "</div>";
        
        //Second Navbar
        html = html + "<div id='navbar2'>";
        html = html + " <a href='/infection_global.html'>Global Infections</a>";
        html = html + "<a href = '/infection_country.html'>Country Infections</a>";
        html = html + "<a href='/infection_state.html' style = 'background-color: rgb(241, 241, 241)'>State/Province Infections</a>";
        html = html + "</div>";
        html = html + "<div id = 'clear'></div>";   
        
        //Form
        html = html + "<form class = action='/infection_state.html' method='post'>";
        html = html + " <div id = 'location_selector'>";
        html = html + "<label for='state_drop' id = 'label1'>Select State:</label><br>";
        html = html + "<select id='state_drop' name='state_drop'>";
        html = html + "   <option selected disabled>Select a State...</option>";
        for(int i = 0; i < state.size();i++){
            html = html + "   <option >" + state.get(i) + "</option>";
        }
        html = html + " </select>";
        html = html + "</div>";
        html = html + " <div id = 'date_selector'>";
        html = html + " <label for = 'startDate'>Select Start Date:</label>";
        html = html + " <input class='form-control' type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22'onchange= 'updatedate()' value = '2020-01-22'> ";
        html = html + "  <label for = 'endDate'>Select End Date:</label>";
        html = html + "  <input class='form-control' id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22' value = '2021-04-22'>";
        html = html + " <input class='form-control' type = 'radio' id ='all_time' name='date' value = 'allday' >";
        html = html + " <label for = 'all_time' style = 'postion:relative; margin-top:8px;'>All Time</label>";
        html = html + "</div>";
        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Show Data</button>";
        html = html + "<input type='reset' class='btn btn-light' id = 'reset'>";
        html = html + "</form>";

        //Gets Input From Form
        String state_drop = context.formParam("state_drop");
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        
        //Creates Table
        if((state_drop == null || state_drop.equals("select"))){
            html = html + "";
        }
        else{
            int cases1 = jdbc.getSSumCases(state_drop,startDate,endDate);
            String date1 = jdbc.getSHighestDate(state_drop,startDate,endDate);
            int population = jdbc.statePopulation(state_drop);
            ArrayList<String>dates = jdbc.getStateDate(state_drop, startDate, endDate);
            ArrayList<Integer>allcases = jdbc.getStateCases(state_drop, startDate, endDate);
            html = html + "<h2 style = 'position: relative;left:2%;'>Infection Rates per State/Province</h2>";
            html = html + "<h3 style = 'position:relative; left:4%'>" + state_drop +  "<h3><h5 style = 'position:relative; left:4%'>("+ startDate + " to " + endDate + ")</h5>";

            html = html + "<div class = 'container1'>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'population'>";
            html = html + "<h6>Population</h6>";
            html = html + "<h4>" + population + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4' id = 'infection'>";
            html = html + "<h6>Total Cases</h6>";
            html = html + "<h4>" + cases1 + "</h4>";
            html = html + "</div>";
            html = html + "<div class='shadow p-4 mb-4 ' id = 'dates' >";
            html = html + "<h6>Date of Highest Case Increase</h6>";
            html = html + "<h4>" + date1 + "</h4>";
            html = html + "</div>";
            html = html + "<h4 style = 'text-align: left;'>Daily Reported Cases</h4>";
            html = html + "<p style = 'font-size: 15px'>This table shows the total number of new COVID-19 cases in Australia reported each day by states and territories and the cumulative number of confirmed COVID-19 cases reported over time.  The Data was collected from 22/01/2020 to 22/04/2021 .</p>";

            html = html + "<div class = 'table2' style = 'width:800px;float:right; margin-right: 170px;'>";
            html = html + "<table class = 'table table-bordered' id = 'mytable'>";
            html = html + "<tr>";
            html = html + "<th>Date</th>";
            html = html + "<th>Total New Cases</th>";
            html = html + "</tr>";
            for(int i = 0; i < dates.size();i++){
            html = html + "<tr>";
            html = html + "<td style = 'font-size: 15px;'>" + dates.get(i) + "</td>";
            html = html + "<td style = 'font-size: 15px;'>" + allcases.get(i) + "</td>";
            html = html + "</tr>";
            }
           
            html = html + "</table>";
            html = html + "</div>";
           
            html = html + "<h4>Comparison Between A States</h4>";
            html = html + "<p style = 'font-size: 15px'>This table shows a comparasion between the total number of COVID-19 cases reported eby states and territories throughout the specifed date range.</p>";
            html = html + "<div class = 'table3' style = 'width:800px; float:right; margin-right: 170px;'>";
            html = html + "<table class='sortable, table table-bordered'>";
            html = html + "<tr>";
            html = html + "<th>State</th>";
            html = html + "<th>Total Cases</th>";
            html = html + "<th>Date of Highest Case Increase</th>";
            for(int i = 0; i < state.size();i++){
                String states = state.get(i);
                int cases = jdbc.getSSumCases(states,startDate,endDate);
                String date = jdbc.getSHighestDate(states,startDate,endDate);
            html = html + "</tr>";
            html = html + "<tr class = 'item'>";
            html = html + "   <td>" + states + "</td>";
            html = html + "   <td>" + cases + "</td>";
            html = html + "   <td>" + date + "</td>";
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
