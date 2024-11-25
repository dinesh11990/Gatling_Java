package DR_GETALL_Endpoint.Case_200;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GETALL_Simulation extends Simulation {

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
            .check(status().is(200))
            .check(jsonPath("$.access_token").saveAs("accessToken"))  // Save the token in the session
    ).exitHereIfFailed();

    //Scenario Definition

    // API request scenario 1 using the token - Get Limit Parameter = 0
    ScenarioBuilder apiScenario = scenario("Get Limit Parameter = 0")
            .exec(getToken)  // First, generate the token
            .exec(http("GET_LIMIT=0")
                    .get(apiBaseUrl + "?offset=0&limit=1")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    .header("User-Agent", "insomnia/9.2.0")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    .header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200))
            );

    // API request scenario 2 using the token - Get Limit Parameter = empty
    ScenarioBuilder apiScenario1 = scenario("Get Limit Parameter = empty")
            .exec(getToken)  // First, generate the token
            .exec(http("GET_LIMIT=EMPTY")
                    .get(apiBaseUrl + "?offset=0&limit= ")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    .header("User-Agent", "insomnia/9.2.0")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    .header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200))
            );

    // API request scenario 3 using the token - Get Limit Parameter = 10
    ScenarioBuilder apiScenario2 = scenario("Get Limit Parameter = 10")
            .exec(getToken)  // First, generate the token
            .exec(http("GET_LIMIT=10")
                    .get(apiBaseUrl + "?offset=0&limit=10")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    .header("User-Agent", "insomnia/9.2.0")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    .header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200))
            );

    {
        // Set up the simulation
        setUp(
                apiScenario.injectOpen(atOnceUsers(10))// Inject users into the scenario
                        .protocols(http.baseUrl(apiBaseUrl)),
                apiScenario1.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl)),
                apiScenario2.injectOpen(atOnceUsers(10))
                        .protocols(http.baseUrl(apiBaseUrl))
        );
    }
}

