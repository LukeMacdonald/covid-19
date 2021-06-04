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
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";

        html = html + "<body>";
        html = html + "<div id='navbar'>";
        html = html + " <a href='/' style = 'background-color:#3189af'>Home</a>";
        html = html + "<a href = '/page2.html'>Facts</a>";
        html = html + "<a href='/infection_global.html'>Infection Rates</a>";
        html = html + "<a href='/page4.html'>Death Rates</a>";
        html = html + "<a href='/cumulative_report.html'>Cumulative Reports</a>";
        html = html + "<a href='/page6.html'>Country Comparasion</a>";
        html = html + "<a href='/worldmap1.html'>World Map</a>";
        html = html + "</div>";

  
        html = html + "<div class = 'index'>";
        
        html = html + "</div>";
        html = html + "<div class = container>";
        html = html + "<a href='/page2.html'><div class = facts>The Top COVID-19 Facts</div></a>";
        html = html + "<a href='/infection_global.html'><div class = infect>Tracking Rate of Infection Throughout Countries</div></a>";
        html = html + "<a href='/page4.html'><div class = death>Tracking Death Rate Throughout Countries</div></a>";
        html = html + "<a href='/cumulative_report.html'><div class = cuml>The Deadly Progression of COVID-19</div></a>";
        html = html + "<a href='/page6.html'><div class = comp >A Comparasion Between Countries</div></a>";
        html = html + "</div>";
        html = html + "<hr>";
        html = html + "<div class = 'footer'></div>";

        html = html + "</body>" + "</html>";  
        context.html(html);
    }

}
