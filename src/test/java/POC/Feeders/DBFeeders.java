package POC.Feeders;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class DBFeeders extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    //CSVfeeder
    //private static FeederBuilder.FileBased<String> csvFeeder = csv("data/CsvFile.csv").circular();

    //JSONfeeder
    //private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/JsonFile.json").circular();


    //customfeeder
    private static Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();

                //To generate numbers from min to max
                //random.nextInt(max - min + 1) + min

                int gameId = rand.nextInt(10 - 1 + 1) + 1;
                return Collections.singletonMap("gameId", gameId);
            }).iterator();


    private ScenarioBuilder scn = scenario("VG DB Test")
            /* .repeat(6).on(
             feed(csvFeeder)
             .exec(http("Get all games - #{gameName}")
                     .get("/videogame/#{gameId}")
                     .check(jmesPath("name").isEL("#{gameName}")))
                     .pause(5)
         );*/


            /*.repeat(1).on(
                feed(jsonFeeder)
                .exec(http("Get all games - #{name}")
                        .get("/videogame/#{id}")
                        .check(jmesPath("name").isEL("#{name}")))
                        .pause(5)
            );*/

            /*.feed(jsonFeeder)
            .exec(http("Get all games - #{name}")
                    .get("/videogame/#{id}")
                    .check(jmesPath("name").isEL("#{name}")))
            .pause(5);*/


            /* .feed(csvFeeder)
             .exec(http("Get all games - #{gameName}")
                     .get("/videogame/#{gameId}")
                     .check(jmesPath("name").isEL("#{gameName}")))
             .pause(5);*/

            //CustomFeeder
            .repeat(10).on(
                    feed(customFeeder)
                            .exec(http("Get game with id - #{gameId}")
                                    .get("/videogame/#{gameId}")));


    //Load Simulation

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
