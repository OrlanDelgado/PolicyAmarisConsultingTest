package cl.demo.polizas.app.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PolicyResourceTest {

    @Test
    void listEmpty() {
        given()
            .when().get("/policies")
            .then()
            .statusCode(200)
            .body("size()", is(0));
    }
}


