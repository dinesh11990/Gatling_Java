package DR_POST_SeparteFiles;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TerminalEstateStatusReport extends Simulation {

    String authURL = "https://auth-upgrade.dev.ppaas.tech";
    String apiBaseUrl = "http://10.17.136.106:8080/v1/reports";

    // Define the ChainBuilder for token generation
    ChainBuilder getToken = exec(http("Get Token")
            .post(authURL + "/auth/realms/m360/protocol/openid-connect/token?tenantName=central-tenant")
            .formParam("grant_type", "password")
            .formParam("client_id", "ppaas-powered-app")
            .formParam("username", "kumar")
            .formParam("password", "@Admin12345")
            .header("content-type", "application/x-www-form-urlencoded")
            .header("Cookie", "KEYCLOAK_LOCALE=fr-FR; KEYCLOAK_LOCALE=fr-FR")
            .check(status().in(200,201))
            .check(jsonPath("$.access_token").saveAs("accessToken"))  // Save the token in the session
    ).exitHereIfFailed();

    // Define the POST request body
    String requestBody = """
            {
              "name": "Terminal Estate Status Report1",
              "description": "This report should return data and some statistics about a terminal or an estate",
               "metrics": [
                  "TENANT NAME"
              ]
            }
    """;

    // API POST request scenario using the token
    ScenarioBuilder postApiScenario = scenario("POST API Request with Token")
            .exec(getToken)  // First, generate the token
            .exec(http("Terminal Estate Status Report")
                    .post(apiBaseUrl)
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("User-Agent", "insomnia/9.2.0")
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")
                    .header("Content-Type", "application/json")
                    .body(StringBody(requestBody))  // Send the request body
                    .check(status().is(201))  // Validate the response status
            );

    {
        // Set up the simulation
        setUp(
                postApiScenario.injectOpen(atOnceUsers(100))  // Inject users into the scenario
        ).protocols(http.baseUrl(apiBaseUrl));
    }
}
