package POC.DataAndReporting;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetAPI extends Simulation {

    String apiBaseUrl = "http://10.17.136.106:8080/analytics/v1/reports";

    HttpProtocolBuilder httpProtocolApi = http
            .baseUrl(apiBaseUrl);

    ScenarioBuilder scn = scenario("Get Limit Parameter = 0")
            .exec(http("GET API LIMIT")
                    .get("?offset=0&limit=1")
                    .header("Authorization", "Bearer #{bearerToken}")
            );

    {
        setUp(scn.injectOpen(atOnceUsers(2))).protocols(httpProtocolApi);
    }
}

