package caching.config.utils;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;

import static caching.config.utils.RestAssureSpecs.requestSpecs;
import static caching.config.utils.RestAssureSpecs.responseSpecs;

@Slf4j
public class TestUtils {

  @BeforeAll
  public static void globalBeforeAll() {

    requestSpecs();
    responseSpecs();
  }


  @AfterAll
  public static void globalAfterAll() {

    RestAssuredWebTestClient.reset();
  }


  public static void globalTestMessage(String subTitle, String testType) {


    if (subTitle.contains("repetition"))
      subTitle = "Error: Provide TestInfo testInfo.getTestMethod().toString()";

    if (subTitle.contains("()]")) {
      subTitle = subTitle.replace("()]", "");
      subTitle = subTitle.substring(subTitle.lastIndexOf(".") + 1);
      subTitle = subTitle.substring(0, 1)
                         .toUpperCase() + subTitle.substring(1);
    }

    String title;

    switch (testType.toLowerCase()) {
      case "class-start" -> title = " STARTING TEST-CLASS...";
      case "class-end" -> title = "...FINISHED TEST-CLASS ";
      case "method-start" -> title = "STARTING TEST-METHOD...";
      case "method-end" -> title = "...FINISHED TEST-METHOD";
      default -> title = "";
    }

    System.out.printf(

         "╔════════════════════════════════════════════════════════════════════╗\n" +
              "║                       %s                                           ║\n" +
              "║ --> Name: %s %38s%n" +
              "╚════════════════════════════════════════════════════════════════════╝\n"
         ,
         title, subTitle, "║"
    );
  }


  public static void globalContainerMessage(MongoDBContainer container, String typeTestMessage) {

    if (container != null) {
      String title;
      switch (typeTestMessage.toLowerCase()) {
        case "container-start" -> title = "STARTING TEST-CONTAINER...";
        case "container-end" -> title = "...FINISHED TEST-CONTAINER";
        case "container-state" -> title = "  ...TEST'S TC-CONTAINER  ";
        default -> title = "";
      }

      System.out.printf(
           "╔═══════════════════════════════════════════════════════════════════════╗\n" +
                "║ --> Name: %s\n" +
                "║ --> Url: %s\n" +
                "║ --> Running: %s\n" +
                "╚═══════════════════════════════════════════════════════════════════════╝\n\n"
           ,
           title,
           container.getContainerName(),
           container.getReplicaSetUrl(),
           container.isRunning()
      );
    }
  }


  public static void globalComposeServiceContainerMessage(
       DockerComposeContainer<?> compose,
       String service,
       Integer port) {

    if (compose != null) {
      System.out.printf(

           "╔═══════════════════════════════════════════════════════════════════════\n" +
                "║                           %s                        ║\n" +
                "║ --> Service: %s\n" +
                "║ --> Host: %s\n" +
                "║ --> Port: %s\n" +
                "║ --> Created: %s\n" +
                "║ --> Running: %s\n" +
                "╚═══════════════════════════════════════════════════════════════════════\n\n"
           ,
           "TC-CONTAINER-COMPOSE",
           service,
           compose.getServiceHost(service, port),
           compose.getServicePort(service, port),
           compose.getContainerByServiceName(service + "_1")
                  .get()
                  .isCreated(),
           compose.getContainerByServiceName(service + "_1")
                  .get()
                  .isRunning()
      );
    }
  }

}