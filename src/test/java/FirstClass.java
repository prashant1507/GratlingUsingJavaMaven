// required for Gatling core structure DSL
import io.gatling.javaapi.core.*;
import static io.gatling.javaapi.core.CoreDsl.*;

// required for Gatling HTTP DSL
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.http.HttpDsl.*;

// can be omitted if you don't use jdbcFeeder
import io.gatling.javaapi.jdbc.*;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

import java.time.Duration;

// https://gatling.io/docs/gatling/reference/current

public class FirstClass extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0");

//    FeederBuilder.Batchable<String> feeder = csv("src\\gatling\\resources\\search.csv").random();
    ChainBuilder chainBuilder = exec(http("request_1").get("/"));

    ScenarioBuilder scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
            .exec(chainBuilder)
            .pause(7) // Note that Gatling has recorder real time pauses
            .exec(http("request_2")
                    .get("/computers?f=macbook"))
            .pause(2)
            .exec(http("request_3")
                    .get("/computers/6"))
            .pause(3)
            .exec(http("request_4")
                    .get("/"))
            .pause(2)
            .exec(http("request_5")
                    .get("/computers?p=1"))
            .pause(Duration.ofMillis(670))
            .exec(http("request_6")
                    .get("/computers?p=2"))
            .pause(Duration.ofMillis(629))
            .exec(http("request_7")
                    .get("/computers?p=3"))
            .pause(Duration.ofMillis(734))
            .exec(http("request_8")
                    .get("/computers?p=4"))
            .pause(5)
            .exec(http("request_9")
                    .get("/computers/new"))
            .pause(1)
            .exec(http("request_10") // Here's an example of a POST request
                    .post("/computers")
                    .formParam("name", "Beautiful Computer") // Note the triple double quotes: used in Scala for protecting a whole chain of characters (no need for backslash)
                    .formParam("introduced", "2012-05-30")
                    .formParam("discontinued", "")
                    .formParam("company", "37"));

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }

    @Override
    public void before() {
        System.out.println("Simulation is about to start!");
    }

    @Override
    public void after() {
        System.out.println("Simulation is finished!");
    }
}

