package POC.Sessions;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ScriptSimulation_CompleteReference extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    /*// Refactor Gatling code into Methods

    private static ChainBuilder getAllVideoGames =
            //Looping HTTP Calls
            repeat(3).on(
                    exec(http("Get all video games")
                            .get("/videogame")
                            .check(status().in(200, 201, 202))) //Multiple response code check

            );


    private static ChainBuilder getSpecificVideoGames =
            //Looping HTTP Calls
            repeat(5, "myCounter").on(exec(http("Get Specific video game")
                    .get("/videogame/#{myCounter}")
                    .check(status().in(200))) //Multiple response code check
            );*/

    private static ChainBuilder authenticate =
            exec(http("Authenticate")
                    .post("/authenticate")
                    .body(StringBody("{\n" +
                            "  \"password\": \"admin\",\n" +
                            "  \"username\": \"admin\"\n" +
                            "}"))
                    .check(jmesPath("token").saveAs("jwtToken"))

            );

    private static ChainBuilder createNewGame =
            exec(http("Create new game")
                    .post("/videogame")
                    .header("Authorization", "Bearer #{jwtToken}")
                    .body(StringBody("{\n" +
                            "  \"category\": \"Platform\",\n" +
                            "  \"name\": \"Mario\",\n" +
                            "  \"rating\": \"Mature\",\n" +
                            "  \"releaseDate\": \"2012-05-04\",\n" +
                            "  \"reviewScore\": 85\n" +
                            "}"
                    )));

    //Scenarion Definition

    private ScenarioBuilder scn = scenario("VGDBTest-S5")

            .exec(authenticate)
            .exec(createNewGame);
           /* .exec(getAllVideoGames)
            .pause(5)
            .exec(getSpecificVideoGames)
            .pause(5)
            //.repeat(2).on(exec(getAllVideoGames));
            .exec(getAllVideoGames);*/
            /*.exec(http("1st Call - Get all")
                    .get("/videogame")
                    .check(status().is(200))
                    //.check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 5"))) // JSON Path Response Body Validator
                    .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 5")))) // JmesPath Response Body Validator //converting array into list
            .pause(5) //Pause using the integer*/

            /*.exec(http("2nd call - Get specific game")
                    .get("/videogame/1")
                    .check(status().in(200, 201, 202)) //Multiple response code check
                    .check(jmesPath("name").is("Resident Evil 4")))
            .pause(1, 10) //Pause using the range for different user

            //Exract the gameId
            .exec(http("3rd call - Get all video game")
                    .get("/videogame")
                    .check(status().not(404), status().not(500))
                    .check(jmesPath("[1].id").saveAs("gameId")))
            .pause(Duration.ofMillis(4000)) //Specify actual duration, This is more specific pause time

            //Debugging Gatling Session Variables
            /*.exec(
                    session -> {
                        System.out.println(session);
                        System.out.println("gameId set to: " + session.getString("gameId"));
                        return session;
                    }
            )*/

    //Save the Data
            /*.exec(http("Get specific game with Id - #{gameId}")
                    .get("/videogame/#{gameId}")
                    .check(jmesPath("name").is("Gran Turismo 3"))
                    .check(bodyString().saveAs("responseBody")))  // Get the responsebody*/

    //Print the response body
            /*.exec(
                    session -> {
                        System.out.println("Response Body: " + session.getString("responseBody"));
                        return session;
                    }
            );*/

    //Load Simulation

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }


}
