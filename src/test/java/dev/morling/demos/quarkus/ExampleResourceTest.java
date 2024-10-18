package dev.morling.demos.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ExampleResourceTest {

    @Test
    public void testTodoEndpoint() {
        given()
          .when().get("/todo")
          .then()
             .statusCode(200);
    }

}