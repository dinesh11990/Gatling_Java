package LoadSimulation;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class FixedDuration2 extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllScenario =
            exec(http("Get all scenario")
                    .get("/videogame"));

    private static ChainBuilder getSpecificScenario =
            exec(http("Get specific scenario")
                    .get("/videogame/2"));

    private ScenarioBuilder scn = scenario("GameDB")
            .forever().on(
            exec(getAllScenario)
            .pause(5)
            .exec(getSpecificScenario)
            .pause(5)
            .exec(getAllScenario)

            );
    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),//Pause for 5seconds
                        atOnceUsers(10),
                        rampUsers(20).during(30)
                ).protocols(httpProtocol)
        ).maxDuration(60); //Finally, with maxDuration you can force your run to terminate based on a duration limit, even if some virtual users are still running. It is useful if you need to bound the duration of your simulation when you can’t predict it.

    }
}
