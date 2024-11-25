package DR_GET_ID_Endpoint.Case_200;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GETID_Simulation extends Simulation {

    String authURL = "https://auth-upgrade.dev.ppaas.tech";
    String apiBaseUrl = "http://10.17.136.106:8080/v1/reports/0191d61a-a2d8-7d99-ada6-46612e27b587";
    String apiBaseUrl1 = "http://10.17.136.106:8080/v1/reports/0191d61a-a2d8-7d99-ada6-46612e27b587/results/latest/download";

    // Define the ChainBuilder for token generation
    ChainBuilder getToken = exec(http("Get Token")
            .post(authURL + "/auth/realms/m360/protocol/openid-connect/token?tenantName=central-tenant")
            .formParam("grant_type", "password")
            .formParam("client_id", "ppaas-powered-app")
            .formParam("username", "kumar")
            .formParam("password", "@Admin12345")
            .header("content-type", "application/x-www-form-urlencoded")
            .header("Cookie", "KEYCLOAK_LOCALE=fr-FR; KEYCLOAK_LOCALE=fr-FR")
            .check(status().is(200))
            .check(jsonPath("$.access_token").saveAs("accessToken"))  // Save the token in the session
    ).exitHereIfFailed();

    //Scenario Definition

    // API request scenario 1 using the token - Get Report In Default Format
    ScenarioBuilder apiScenario = scenario("Get Report In Default Format")
            .exec(getToken)  // First, generate the token
            .exec(http("Get Report In Default Format")
                    .get(apiBaseUrl + "/results/latest/download")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("User-Agent", "insomnia/9.2.0")  // Use the token in the header
                    .header("X-Correlation-ID", "019106f8-6323-79de-a47d-df36f7f9f690")  // Use the token in the header
                    //.header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200)))
            .pause(5
            );

    // API request scenario 2 using the token - Get Report In JSON Format
    ScenarioBuilder apiScenario1 = scenario("Get Report In JSON Format")
            .exec(getToken)  // First, generate the token
            .exec(http("Get Report In JSON Format")
                    .get(apiBaseUrl1 + "?exportFormat=json")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    .header("exportFormat", "json")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    //.header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200)))
            .pause(5
            );

    // API request scenario 3 using the token - Get Report In CSV Format
    ScenarioBuilder apiScenario2 = scenario("Get Report In CSV Format")
            .exec(getToken)  // First, generate the token
            .exec(http("Get Report In CSV Format")
                    .get(apiBaseUrl1 + "?exportFormat=csv")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("exportFormat", "json")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    //.header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200)))
            .pause(5
            );

    {
        // Set up the simulation
        setUp(
                apiScenario.injectOpen(atOnceUsers(10))// Inject 2users into the scenario
                        .protocols(http.baseUrl(apiBaseUrl)),
                apiScenario1.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl)),
                apiScenario2.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl))

        );
    }
}

