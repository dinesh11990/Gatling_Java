package POC.DataAndReporting;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CombinedFile2 extends Simulation {

    String authBaseUrl = "https://auth-upgrade.dev.ppaas.tech";
    String apiBaseUrl = "http://10.17.136.106:8080/analytics/v1/reports";

    // Merge the base URLs and other settings into a single HttpProtocol
    HttpProtocolBuilder httpProtocol = http
            .baseUrl(authBaseUrl)
            .acceptHeader("application/json")
            .contentTypeHeader("application/x-www-form-urlencoded");

    // Token generation scenario
    HttpRequestActionBuilder getToken = http("Get Bearer Token")
            .post("/auth/realms/m360/protocol/openid-connect/token?tenantName=central-tenant")
            .formParam("grant_type", "password")
            .formParam("client_id", "ppaas-powered-app")
            .formParam("username", "kumar")
            .formParam("password", "@Admin12345")
            .check(jsonPath("$.access_token").saveAs("bearerToken"));

    ScenarioBuilder tokenScenario = scenario("Token Retrieval Scenario")
            .exec(getToken);

    // API request scenario
    ScenarioBuilder apiScenario = scenario("Get Limit Parameter = 0")
            .exec(http("GET API LIMIT")
                    .get(apiBaseUrl + "?offset=0&limit=1")
                    .header("Authorization", "Bearer #{bearerToken}")
            );

    {
        setUp(tokenScenario.injectOpen(atOnceUsers(1)).andThen(
                apiScenario.injectOpen(atOnceUsers(2))
        )).protocols(httpProtocol);
    }
}
