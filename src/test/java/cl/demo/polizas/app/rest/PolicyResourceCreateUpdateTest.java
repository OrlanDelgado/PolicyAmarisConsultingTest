package cl.demo.polizas.app.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class PolicyResourceCreateUpdateTest {

    @Test
    void createAndUpdateStatusFlow() {
        final String payload = "{\n" +
                "  \"rutTitular\": \"12.345.678-5\",\n" +
                "  \"fechaEmision\": \"2024-01-10\",\n" +
                "  \"planSalud\": \"Plan Oro\",\n" +
                "  \"prima\": 34990.00\n" +
                "}";

        final String id =
            given()
                .contentType("application/json")
                .body(payload)
            .when()
                .post("/policies")
            .then()
                .statusCode(201)
                .header("Location", containsString("/policies/"))
                .body("estado", is("emitida"))
                .extract().path("id");

        given()
            .contentType("application/json")
            .body("{ \"estado\": \"activa\" }")
        .when()
            .put("/policies/" + id + "/status")
        .then()
            .statusCode(200)
            .body("estado", is("activa"));

        given()
            .contentType("application/json")
            .body("{ \"estado\": \"emitida\" }")
        .when()
            .put("/policies/" + id + "/status")
        .then()
            .statusCode(409);
    }
}


