package POC.DataAndReporting;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Token extends Simulation {

    String authBaseUrl = "https://auth-upgrade.dev.ppaas.tech";

    HttpProtocolBuilder httpProtocolAuth = http
            .baseUrl(authBaseUrl)
            .acceptHeader("application/json")
            .contentTypeHeader("application/x-www-form-urlencoded");

    HttpRequestActionBuilder getToken = http("Get Bearer Token")
            .post("/auth/realms/m360/protocol/openid-connect/token?tenantName=central-tenant")
            .formParam("grant_type", "password")
            .formParam("client_id", "ppaas-powered-app")
            .formParam("username", "kumar")
            .formParam("password", "@Admin12345")
            .check(jsonPath("$.access_token").saveAs("bearerToken"));

    ScenarioBuilder scn = scenario("Token Retrieval Scenario")
            .exec(getToken);

    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocolAuth);
    }
}
