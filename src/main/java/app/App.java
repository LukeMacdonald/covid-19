package app;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

public class App {

    public static final int         JAVALIN_PORT    = 7000;
    public static final String      CSS_DIR         = "css/";
    public static final String      IMAGES_DIR      = "images/";

    public static void main(String[] args) {
        
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/help/routes"));
            
            config.addStaticFiles(CSS_DIR);        
            config.addStaticFiles(IMAGES_DIR);
        }).start(JAVALIN_PORT);


  
        configureRoutes(app);
    }
    public static void configureRoutes(Javalin app) {

        //lukes pages
        app.get(Home.URL, new Home());
        // app.get(Page2.URL, new Page2());
        app.get(CountryInfect.URL, new CountryInfect());
        app.get(StateInfections.URL, new StateInfections());
        app.get(GlobalInfect.URL, new GlobalInfect());
        // app.get(Page4.URL, new Page4());
        app.get(Cumulative.URL, new Cumulative());
        app.get(SimilarCountries.URL,new SimilarCountries());
        // app.get(Page6.URL, new Page6());
        app.get(map.URL, new map());

        //doms pages
        app.get(CovidFacts.URL, new CovidFacts());
        app.get(CountryDeaths.URL, new CountryDeaths());
        app.get(StateDeaths.URL, new StateDeaths());
        app.get(CompareCountries.URL, new CompareCountries());
        app.get(CompareStates.URL, new CompareStates());
        app.get(CompareCountriesAdvanced.URL, new CompareCountriesAdvanced());
        app.get(CompareStatesAdvanced.URL, new CompareStatesAdvanced());
        app.get(CompareStateCountryAdvanced.URL, new CompareStateCountryAdvanced());

        //lukes pages
        app.post(Home.URL, new Home());
        // app.post(Page2.URL, new Page2());
        app.post(CountryInfect.URL, new CountryInfect());
        app.post(StateInfections.URL, new StateInfections());
        app.post(GlobalInfect.URL, new GlobalInfect());
        // app.post(Page4.URL, new Page4());
        app.post(Cumulative.URL, new Cumulative());
        app.post(SimilarCountries.URL,new SimilarCountries());
        // app.post(Page6.URL, new Page6());

        //doms pages
        app.post(CountryDeaths.URL, new CountryDeaths());
        app.post(StateDeaths.URL, new StateDeaths());
        app.post(CompareCountries.URL, new CompareCountries());
        app.post(CompareStates.URL, new CompareStates());
        app.post(CompareCountriesAdvanced.URL, new CompareCountriesAdvanced());
        app.post(CompareStatesAdvanced.URL, new CompareStatesAdvanced());
        app.post(CompareStateCountryAdvanced.URL, new CompareStateCountryAdvanced());
        
    }

}
