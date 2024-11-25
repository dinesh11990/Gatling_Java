package POC.DataAndReporting;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ApiSimulation extends Simulation {

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

    // API request scenario using the token
    ScenarioBuilder apiScenario = scenario("Get Limit Parameter = 0")
            .exec(getToken)  // First, generate the token
            .exec(http("GET API LIMIT")
                    .get(apiBaseUrl + "?offset=0&limit=1")
                    .header("Authorization", "Bearer #{accessToken}")  // Use the token in the header
                    .header("User-Agent", "insomnia/9.2.0")  // Use the token in the header
                    .header("X-Correlation-ID", "16427231-b0bc-4d2b-91b2-514066c153c1")  // Use the token in the header
                    .header("Content-Type", "application/json")  // Use the token in the header
                    .check(status().is(200))
            );

    {
        // Set up the simulation
        setUp(
                apiScenario.injectOpen(atOnceUsers(2))  // Inject 2 users into the scenario
        ).protocols(http.baseUrl(apiBaseUrl));
    }
}

