package app;



import io.javalin.http.Context;
import io.javalin.http.Handler;

public class Home implements Handler {

   
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
      
        String html = "<html>";

    
        html = html + "<head>" + 
               "<title>Coronavirus</title>";
               html = html + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"covidlogo.png\"/>";
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";
        html = html + "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css'>";

        html = html + "<body>";
        html = html + "<div id='navbar'>";
        html = html + "<a href='/'style = 'background-color:#3189af'>Home</a>";
        html = html + "<a href='/covidfacts.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/countrydeaths.html'>Death Rates</a>";
        html = html + "<a href='/cumalative_global.html'>Cumulative Reports</a>";
        html = html + "<a href='/comparecountries.html'>Country Comparison</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";

  
        html = html + "<div class = 'index'>";
        html = html + "</div>";
        html = html + "<div class = container>";
        html = html + "<div class='shadow p-4 mb-4 ' id = 'facts'>";
        html = html + "<a href='/covidfacts.html'>The Top COVID-19 Facts</a>"; 
        html = html + "<br>";
        html = html + "<p> This page contains information regarding important facts relating to COVID-19. This includes the global number of cases, deaths and mortaility rates.</p>";
        html = html + "</div>"; 
        html = html + "<div class='shadow p-4 mb-4 ' id = 'infect'>";
        html = html + "<a href='/infection_global.html'>Tracking Rate of Infection</a>";
        html = html + "<br>";
        html = html + "<p> This page contains information regarding the number of COVID-19 cases globally, per country and per state.</p>";
        html = html + "<a href='/infection_global.html'style = 'font-size:14px;'>View Global Cases</a>"; 
        html = html + "<br>";
        html = html + "<a href='/infection_country.html'style = 'font-size:14px;'>View Cases Per Country</a>"; 
        html = html + "<br>";
        html = html + "<a href='/infection_state.html'style = 'font-size:14px;'>View Cases Per State/Province</a>"; 
        html = html + "</div>"; 
        html = html + "<div class='shadow p-4 mb-4 ' id = 'death'>";
        html = html + "<a href='/countrydeaths.html'>Tracking Death Rate</a>";
        html = html + "<p> This page contains information regarding the number of COVID-19 deaths per country and per state.</p>"; 
        html = html + "<br>";
        html = html + "<a href='/countrydeaths.html'style = 'font-size:14px;'>View Deaths Per Country</a>"; 
        html = html + "<br>";
        html = html + "<a href='/statedeaths.html'style = 'font-size:14px;'>View Deaths Per State/Province</a>"; 
        html = html + "</div>"; 
        html = html + "<div class='shadow p-4 mb-4 ' id = 'cumul'>";
        html = html + "<a href='/cumalative_global.html '>Viewing the Cumulative Data of COVID-19</a>"; 
        html = html + "<p> This page contains information regarding:";
        html = html + "<ul> <li>the total number of COVID-19 cases and deaths<li>death rates and rate of infections for each country. <li> Comparing cumulative data between countries of similar climiate<li> Comparing countries within a certain distance of eachother</ul></p>";
        html = html + "<a href='/cumulative_report.html'style = 'font-size:14px;'>View Country Cumulative Report</a><br>";
        html = html + "<a href='/worldmap1.html'style = 'font-size:14px;'>View Global Map</a>";
        html = html + "</div>"; 
        html = html + "<div class='shadow p-4 mb-4 ' id = 'comp'>";
        html = html + "<a href='/comparecountries.html'>A Comparasion Between Regions</a>";
        html = html + "<p> This page allows for compararaison of COVID-19 data for:";
        html = html + "<ul> <li>Similar mortality rates<li>Similar total number of cases and deaths<li> Comparing cumulative data between countries of similar climiate<li> Similar highest death day</ul></p>";
        html = html + "<a href='/comparecountries.html'style = 'font-size:14px;'>Compare Countries</a><br>";
        html = html + "<a href='/comparestates.html'style = 'font-size:14px;'>Compare States</a>";
        html = html + "</div>";  
        html = html + "</div>"; 
        html = html + "</div>";
       

        html = html + "</body>" + "</html>";  
        context.html(html);
    }

}
