package DR_POST_Endpoint.Case_200;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class POST_Simulation extends Simulation {

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
            .check(status().in(200, 201))
            .check(jsonPath("$.access_token").saveAs("accessToken"))  // Save the token in the session
    ).exitHereIfFailed();

    // Define the POST request body - Description field is empty
    String requestBody = """
            {
              "name": "Terminal Estate Status Report1",
                        "description": "",
                         "metrics": [
                            "TENANT NAME"
              ]
            }
            """;

    // Define the POST request body - NameFieldWithMaxLength
    String requestBody1 = """
            {
              "name": "This report should return data and some statistics about a terminal or an estate",
              "description": "This report should return data and some statistics about a terminal or an estate",
              "metrics": [    
                  "TENANT NAME"
              ]
            }
            """;

    // Define the POST request body - TerminalEstateStatusReport
    String requestBody2 = """
                    {
                      "name": "Terminal Estate Status Report1",
                      "description": "This report should return data and some statistics about a terminal or an estate",
                       "metrics": [
                          "TENANT NAME"
                      ]
                    }
            """;

    //Scenario Definition

    // API POST request scenario using the token
    ScenarioBuilder postApiScenario = scenario("Description Field is Empty")
            .exec(getToken)  // First, generate the token
            .exec(http("Description Field is Empty")
                    .post(apiBaseUrl)
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("User-Agent", "insomnia/9.2.0")
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")
                    .header("Content-Type", "application/json")
                    .body(StringBody(requestBody))  // Send the request body
                    .check(status().is(201))  // Validate the response status
            );

    // API POST request scenario 1 using the token -
    ScenarioBuilder postApiScenario1 = scenario("Name Field with Max Length")
            .exec(getToken)  // First, generate the token
            .exec(http("Name Field with Max Length")
                    .post(apiBaseUrl)
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("User-Agent", "insomnia/9.2.0")
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")
                    .header("Content-Type", "application/json")
                    .body(StringBody(requestBody1))  // Send the request body
                    .check(status().is(201))  // Validate the response status
            );

    // API POST request scenario using the token
    ScenarioBuilder postApiScenario2 = scenario("Terminal Estate Status Report")
            .exec(getToken)  // First, generate the token
            .exec(http("Terminal Estate Status Report")
                    .post(apiBaseUrl)
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    //.header("User-Agent", "insomnia/9.2.0")
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")
                    .header("Content-Type", "application/json")
                    .body(StringBody(requestBody2))  // Send the request body
                    .check(status().is(201))  // Validate the response status
            );

    {
        // Set up the simulation
        setUp(
                postApiScenario.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl)),
                postApiScenario1.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl)),
                postApiScenario2.injectOpen(atOnceUsers(10))// Inject users into the scenario
                        .protocols(http.baseUrl(apiBaseUrl))
        );

    }
}
