package package2;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpDsl;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.apache.commons.httpclient.HttpStatus;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;

// https://rieckpil.de/write-gatling-performance-tests-with-java/
// https://gatling.io/docs/gatling/reference/current
// To Run using maven - -Dgatling.simulationClass=<className>, or enable runMultipleSimulations in your pom.xml
// To see all gradle help - 'mvn gatling:help -Ddetail=true -Dgoal=test'
public class Test1Performance extends Simulation {

  String url = "https://computer-database.gatling.io";
  String headers = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
  String encoding = "gzip, deflate";
  String language = "en-IN,en-US;q=0.9,en;q=0.8,hi-IN;q=0.7,hi;q=0.6,en-GB;q=0.5";
  String dnt = "1";
  String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36";

  String computerToSearch = "macbook";
  String idForComputerToEdit1 = "330";
  String idForComputerToEdit2 = "518";

  HttpProtocolBuilder httpProtocolBuilder = HttpDsl.http
    .baseUrl(url)
    .acceptHeader(headers)
    .acceptEncodingHeader(encoding)
    .acceptLanguageHeader(language)
    .doNotTrackHeader(dnt)
    .userAgentHeader(userAgent);

  ChainBuilder navigateToUrl = CoreDsl.exec(HttpDsl.http("Navigate To URL").get("/"));
  ChainBuilder searchComputer = CoreDsl.exec(HttpDsl.http("Search Computer " + computerToSearch).get("/computers?f=" + computerToSearch));
  ChainBuilder editComputer2 = CoreDsl.exec(HttpDsl.http("Edit Computer " + idForComputerToEdit2).get("/computers/" + idForComputerToEdit2).check(HttpDsl.status().is(HttpStatus.SC_OK)));

  ScenarioBuilder scenarioBuilder = CoreDsl.scenario("Search Computer")
    .exec(navigateToUrl)
    .pause(Duration.ofSeconds(2))
    .exec(searchComputer)
    .pause(Duration.ofSeconds(2))
    .exec(editComputer2)
    .pause(Duration.ofSeconds(2))
    .exec(
      // Prefer to write using ChainBuilder for better readability
      HttpDsl.http("Navigate to Edit Computer for " + computerToSearch)
        .get("/computers/" + idForComputerToEdit1)
        .check(HttpDsl.status().is(HttpStatus.SC_OK))
    );

  {
    setUp(
      scenarioBuilder.injectOpen(
        atOnceUsers(1),
        rampUsers(1).during(5)
      )).protocols(httpProtocolBuilder
    );
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
