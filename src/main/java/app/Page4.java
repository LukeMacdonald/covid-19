package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;


public class Page4 implements Handler {
    public static final String URL = "/page4.html";

    
    

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "<html>";

        // Add some Header information
        html = html + "<head>" + 
               "<title>Movies</title>";

        // Add some CSS (external file)
        html = html + "<link rel='stylesheet' type='text/css' href='common.css' />";

        // Add the body
        html = html + "<body>";

        // Add HTML for link back to the homepage
        html = html + "<h1>Page 4</h1>";
        html = html + "<p>Return to Homepage: ";
        html = html + "<a href='/'>Link to Homepage</a>";
        html = html + "</p>";

   
        JDBCConnection jdbc = new JDBCConnection();

       
      

     
        html = html + "<h1>Movies</h1>" + "<ul>";

       
        html = html + "</ul>";

        
        html = html + "</body>" + "</html>";


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
