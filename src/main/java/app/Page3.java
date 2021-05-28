package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Page3 implements Handler {

  
    public static final String URL = "/page3.html";

    @Override
    public void handle(Context context) throws Exception {
        JDBCConnection jdbc = new JDBCConnection();
        ArrayList<String> countries = jdbc.getCountries();
        
        String html = "<html>";

        
        html = html + "<head>";
        html = html +"<title>Cases</title>";
        html = html + "<link rel='stylesheet' type='text/css' href='cases.css'/>";

        html = html + "<script>";
        html = html + "function updatedate() {";
        html = html + " var firstdate = document.getElementById('startDate').value;";
        html = html + " document.getElementById('endDate').value = '';";
        html = html + " document.getElementById('endDate').setAttribute('min',firstdate);";
        html = html + "}";
        html = html + "</script>";
        
        html = html + "</head>";
       
        html = html + "<body>";
        
        html = html + "<div id='navbar'>";
        html = html + " <a href='/page1.html'>Home</a>";
        html = html + "<a href = '/page2.html'>Facts</a>";
        html = html + "<a href='/page3.html'>Infection Rates</a>";
        html = html + "<a href='/page4.html'>Death Rates</a>";
        html = html + "<a href='/page5.html'>Cumulative Reports</a>";
        html = html + "<a href='/page6.html'>Country Comparasion</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";

        html = html + "<form action='/page3.html' method='post'>";
        html = html + "<div class='form-group'>";
        html = html + "<label for='country_drop' id = 'label1'>Select Country:</label><br>";
        html = html + "<select id='country_drop' name='country_drop'>";
        html = html + "   <option value = 'select'>Select:</option>";
        for(int i = 0; i < countries.size();i++){
            html = html + "   <option >" + countries.get(i) + "</option>";
        }
        html = html + " </select>";

        html = html + "<div class='form-group' id = 'start'>";
        html = html + " <label for = 'startDate'>Enter Start Date:</label>";
        html = html + " <input class='form-control' type='date' id ='startDate' name='startDate' min = '2020-01-22' max = '2021-04-22' onchange= 'updatedate()' value = '2020-01-22'>";
        html = html + "</div>";
      
        html = html + "<div class='form-group' id = 'end'>";
        html = html + "  <label for = 'endDate'>Enter End Date:</label>";
        html = html + "  <input class='form-control' id='endDate' name='endDate' type = 'date' min = '2020-01-22' max = '2021-04-22'>";
        html = html + "</div>";

        html = html + "<button type='submit' class='btn btn-primary' id = 'submit'>Submit</button>";
        html = html + "<input type='reset' id = 'reset'>";
        html = html + "</form>";
        
        String country_drop = context.formParam("country_drop");
        String startDate = context.formParam("startDate");
        String endDate = context.formParam("endDate");
        if (country_drop == null || country_drop.equals("select")) {
            html = html + "";
        } else {
            ArrayList<Integer> movies = jdbc.getSumCases(country_drop,startDate,endDate);
            html = html + movies.get(0);
            html = html + getallCases(country_drop, startDate, endDate);
        }

        html = html + "<script>";
        html = html + "window.onscroll = function() {scrollFunction()};";

        html = html + "function scrollFunction() {";
        html = html + "  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {";
        html = html + "   document.getElementById('navbar').style.top = '0'";
        html = html + "  } else {";
        html = html + "    document.getElementById('navbar').style.top = '-50px';";
        html = html + "  }";
        html = html + "}";
                   
        html = html + "</script>";

       
        html = html + "</body>" + "</html>";


        context.html(html);
    }

public String getallCases(String country,String date1,String date2){
    if(country == null){
        String html = "";
        html = html + "<h2><i></i></h2>";
        return html;
    }else{
        JDBCConnection jdbc = new JDBCConnection();
        
        String html = "";
        ArrayList<String> dates = jdbc.getDate(country, date1, date2);
        ArrayList<Integer> cases = jdbc.getCases(country, date1, date2);

        html = html + "<table>";
        html = html + "<tr>";
        html = html + "<th COLSPAN='2'><h3>" + country +  "<h3></th>";
        html = html + "</tr>";
        html = html + "<tr>";
        html = html + "<th>Dates</th>";
        html = html + "<th>Cases:</th>";
        html = html + "</tr>";
        for(int i = 0; i < dates.size();i++){
            html = html + "<tr>";
            html = html + "<td>" + dates.get(i) + "</td>";
            html = html + "<td>" + cases.get(i) + "</td>";
            html = html + "</tr>";
        }
        html = html + "</table>";
        return html;
    }
}
}


    
    
         
       


     
