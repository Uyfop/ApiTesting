package org.example.TypeTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.DataLists.MTGCardData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class MTGTypeTest {

    MTGCardData data = new MTGCardData();

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    void getAllTypes_Success() {
        List<String> expectedTypes = data.retrieveCardTypes();
        given()
                .when()
                .get("/types")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("types", not(empty()))
                .body("types", hasItems(expectedTypes.toArray(new String[0])));
    }


    @Test
    void getAllSubtypes_Success() {
        List<String> expectedSubtypes = data.retrieveCardSubtypes();

        given()
                .when()
                .get("/subtypes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("subtypes", not(empty()))
                .body("subtypes", hasItems(expectedSubtypes.toArray(new String[0])));
    }

    @Test
    void getAllSupertypes_Success() {
        List<String> expectedSupertypes = data.retrieveCardSupertypes();

        given()
                .when()
                .get("/supertypes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("supertypes", not(empty()))
                .body("supertypes", hasItems(expectedSupertypes.toArray(new String[0])));
    }

    @Test
    void getAllFormats_Success() {
        List<String> expectedFormats = data.retrieveFormats();

        given()
                .when()
                .get("/formats")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("formats", not(empty()))
                .body("formats", hasItems(expectedFormats.toArray(new String[0])));
    }
}
