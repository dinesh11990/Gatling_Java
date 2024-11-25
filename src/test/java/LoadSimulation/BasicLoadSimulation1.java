package LoadSimulation;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class BasicLoadSimulation1 extends Simulation {

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
            .exec(getAllScenario)
            .pause(5)
            .exec(getSpecificScenario)
            .pause(5)
            .exec(getAllScenario);

    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),//HoldOn for 5seconds
                        //atOnceUsers(5), //5 Users in shot
                        //rampUsers(10).during(20) //10 Users 20 Seconds //10 users ramping away for 20 seconds
                        //constantUsersPerSec(10).during(10)  // 10 Users will hit in constant regular intervals, ie Per sec 10users will be injecting for 5 sec, so 5*10=50 APIs
                        //constantUsersPerSec(5).during(10).randomized() //No proper intervals all are randomized, ie 10 Users will hit in constant randomized intervals, ie Per sec 10users will be injecting for 5 sec, so 5*10=50 APIs
                        //rampUsersPerSec(5).to(10).during(15) //Rampup from 5 to 10 users in regular intervals and all this happens between 15 seconds
                        rampUsersPerSec(5).to(10).during(20).randomized() //Rampup from 5 to 10 users in randomized intervals and all this happens between 20 seconds
                ).protocols(httpProtocol));

        //stressPeakUsers  //It gonna hit 50 users in the timespan of 20 seconds //Per second it might hit 50+ users ie too much of api hit will happen
                /*scn.injectOpen(stressPeakUsers(users=50).during(20)))
                .protocols(httpProtocol)*/

        //ConstantConcurrentUsers  //
                /*scn.injectOpen(
                //constantConcurrentUsers(users=20).during(20))) // Constantly put the concurrent users for 20 users

        //ConstantConcurrentUsers  //
            /*scn.injectOpen(
                //rampConcurrentUsers(from=10).to(20).during(20) // ramp up concurrent (happening at same time) users from 10 to 20
                .protocols(httpProtocol)*/

        //FixedDurationTest //
         /*scn.injectOpen(rampUsers(users=20).during(60)))
         .maxDuration(10) //eventhough duration is for 60sec it ll control the maxduration to 10 secs, code will execute after 10 seconds and complete within 10 seconds
                .protocols(httpProtocol)*/

        //Throttling - It also similar to the max duration, where it will interrupt of whatever the time we have declared during our injection
            /*scn.injectOpen(
                //constantConcurrentUsers(users=20).during(20)  // After injection is happened
                ))
                .throttle(  //Now asking the throttle to reach up to 100 request per second, it means per second it will inject 100 under requests for 10seconsd
                reachRps(target = 100).in(duration =10),
                holdFor(duration=3), now it ll wait for 3 seconds
                jumpToRps(target=50) // now jump to request per second to 50
                holdFor(duration=3) now hold for 3 seconds

                )
             */

    }
}
