package POC.Sessions;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class VGSimulation extends Simulation{

    //Http config

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    //Scenarion Definition

    private ScenarioBuilder scn = scenario("VG DB Test")
            .exec(http("Get all games")
                    .get("/videogame"));

    //Load Simulation

    {
        setUp(
                scn.injectOpen(atOnceUsers(100))
        ).protocols(httpProtocol);
    }


}
