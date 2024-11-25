package LoadSimulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class RuntimeParameters3 extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS","5"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION","10"));
    private static final int TEST_DURATION = Integer.parseInt(System.getProperty("TEST_DURATION","20"));

    //It will run at the start of the load test
    @Override
    public void before(){

        System.out.printf("Running test with %d users%n", USER_COUNT);
        System.out.printf("Ramping users over %d seconds%n", RAMP_DURATION);
        System.out.printf("Total test duration: %d users%n", TEST_DURATION);
    }

    private static ChainBuilder getAllScenario =
            exec(http("Get all scenario")
                    .get("/videogame"));

    private static ChainBuilder getSpecificScenario =
            exec(http("Get specific scenario")
                    .get("/videogame/2"));

    private ScenarioBuilder scn = scenario("GameDB")
            .exec(getAllScenario)
            .pause(5)
            .exec(getSpecificScenario)
            .pause(5)
            .exec(getAllScenario);
    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),//Pause for 5seconds
                 rampUsers(USER_COUNT).during(RAMP_DURATION)
                ).protocols(httpProtocol)
        ).maxDuration(TEST_DURATION); //Finally, with maxDuration you can force your run to terminate based on a duration limit, even if some virtual users are still running. It is useful if you need to bound the duration of your simulation when you can’t predict it.



    }
}