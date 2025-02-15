package org.example.CardsTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MTGCardsAdvanceTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    void getCardsByExactName_Success() {
        given()
                .queryParam("name", "Ancestor's Chosen")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cards", not(empty()))
                .body("cards.name", hasItem("Ancestor's Chosen"));
    }

    @Test
    void getCardsByPartialName_Success() {
        given()
                .queryParam("name", "avacyn")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cards", not(empty()))
                .body("cards.name", everyItem(containsString("Avacyn")));
    }

    @Test
    void getCardsByForeignName_Success() {
        given()
                .queryParam("foreignNames", "Elegido de la Antepasada")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cards", not(empty()))
                .body("cards.foreignNames.flatten().name", hasItem("Elegido de la Antepasada"));
    }
    @Test
    void getCardsByNameNotFound_Failure() {
        given()
                .queryParam("name", "NonExistentCardName")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .body("cards", hasSize(0));
    }

    @Test
    void getCardsByInvalidForeignName_Failure() {
        given()
                .queryParam("foreignNames", "Invalid Foreign Card")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }


}
