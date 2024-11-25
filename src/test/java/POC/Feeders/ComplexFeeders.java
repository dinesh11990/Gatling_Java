package POC.Feeders;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ComplexFeeders extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    //csvfeeder
    //private static FeederBuilder.FileBased<String> csvFeeder = csv("data/CsvFile.csv").circular();

    //jsonfeeder
    //private static FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("data/JsonFile.json").circular();


    public static LocalDate randomDate() {
        int hundredYears = 100 * 365;
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));

    }


    //CustomFeeder
    private static Iterator<Map<String, Object>> customFeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                Random rand = new Random();

                //To generate numbers from min to max
                //random.nextInt(max - min + 1) + min

                int gameId = rand.nextInt(10 - 1 + 1) + 1;
                String gameName = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
                String releaseDate = randomDate().toString();
                int reviewScore = rand.nextInt(100);
                String category = RandomStringUtils.randomAlphanumeric(5) + "-category";
                String rating = RandomStringUtils.randomAlphanumeric(4) + "-rating";

                HashMap<String, Object> hmap = new HashMap<String, Object>();
                hmap.put("gameId", gameId);
                hmap.put("gameName", gameName);
                hmap.put("releaseDate", releaseDate);
                hmap.put("reviewScore", reviewScore);
                hmap.put("category", category);
                hmap.put("rating", rating);
                return hmap;
            }).iterator();

    static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jmesPath("token").saveAs("jwtToken")));

    private ScenarioBuilder scn = scenario("VG DB Test")
            .exec(authenticate)
            //CustomFeeder
            .repeat(10).on(
                    feed(customFeeder)

                            .exec(http("Create New Game - #{gameName}")
                                    .post("/videogame")
                                    .header("authorization", "Bearer #{jwtToken}")
                                    .body(ElFileBody("bodies/newTemplate.json")).asJson()
                                    .check(bodyString().saveAs("responseBody"))

                            )

                            .exec(session -> {

                                System.out.println(session.getString("responseBody"));
                                return session;
                            })
            );


    //Load Simulation

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
