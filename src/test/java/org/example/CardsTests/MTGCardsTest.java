package org.example.CardsTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.DataLists.MTGCardData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;

public class MTGCardsTest {
    MTGCardData data = new MTGCardData();
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    void getAllCards_Success() {
        given()
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cards", not(empty()));
    }

    @Test
    void getCardById_Success() {
        Response response =
                given()
                        .when()
                        .get("/cards/10")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        String cardName = response.jsonPath().getString("card.name");
        assertEquals("Crystal Rod", cardName);
    }

    @Test
    void getCardById_NotFound() {
        given()
                .pathParam("id", "4444444")
                .when()
                .get("/cards/{id}")
                .then()
                .statusCode(404);
    }


    @Test
    void pagination_Success() {
        given()
                .queryParam("page", 1)
                .queryParam("pageSize", 10)
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .body("cards.size()", equalTo(10));
    }

    @Test
    void getCardsByColor_Success() {
        given()
                .queryParam("colors", "green|black")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .body("cards", not(empty()))
                .body("cards.colors", everyItem(anyOf(hasItem("Green"), hasItem("Black"))));
    }


    @Test
    void getCardsByColor_Failure() {
        given()
                .queryParam("colors", "NOTEXIST")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }
    @Test
    void getCardsWithEmptyResult_Success() {
        given()
                .queryParam("name", "NotExists")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .body("cards", hasSize(0));
    }

    @Test
    void getAllCardsOfType_Success() {
        given()
                .queryParam("type", "Artifact")
                .when()
                .get("/cards")
                .then()
                .statusCode(200)
                .body("cards", not(empty()))
                .body("cards.type", everyItem(containsString("Artifact")));
    }

    @Test
    void getAllCardsOfType_Failure() {
        given()
                .queryParam("type", "NOTEXISTENT")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

    @Test
    void getAllCardTypes_Success() {
        List<String> cardTypes = data.retriveCardTypes();
        for (String type : cardTypes) {
            System.out.println("Testing card type: " + type);

            Response response = given()
                    .queryParam("type", type)
                    .when()
                    .get("/cards");

            if (!response.jsonPath().getList("cards").isEmpty()) {
                response.then()
                        .statusCode(200)
                        .body("cards", not(empty()))
                        .body("cards.type", everyItem(containsString(type)));
            } else {
                System.out.println("No cards found for type: " + type);
            }
        }
    }

    @Test
    void getAllCardRarities_Success() {
        List<String> cardRarities = data.retrieveCardRarities();
        for (String rarity : cardRarities) {
            System.out.println("Testing rarity: " + rarity);

            Response response = given()
                    .queryParam("rarity", rarity)
                    .when()
                    .get("/cards");

            if (!response.jsonPath().getList("cards").isEmpty()) {
                response.then()
                        .statusCode(200)
                        .body("cards", not(empty()))
                        .body("cards.rarity", everyItem(equalTo(rarity)));
            } else {
                System.out.println("No cards found for rarity: " + rarity);
            }
        }
    }

    @Test
    void getAllCardRarities_Failure() {
        given()
                .queryParam("rarity", "NOTEXISTENT")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

    @Test
    void getAllCardLayouts_Success() {
        List<String> cardLayouts = data.retrieveCardLayouts();
        for (String layout : cardLayouts) {
            System.out.println("Testing Layouts: " + layout);

            Response response = given()
                    .queryParam("layout", layout)
                    .when()
                    .get("/cards");

            if (!response.jsonPath().getList("cards").isEmpty()) {
                response.then()
                        .statusCode(200)
                        .body("cards", not(empty()))
                        .body("cards.layout", everyItem(equalTo(layout)));
            } else {
                System.out.println("No cards found for layout: " + layout);
            }
        }
    }

    @Test
    void getAllCardLayouts_Failure() {
        given()
                .queryParam("layouts", "NOTEXISTENT")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

    @Test
    void pagination_Failure() {
        given()
                .queryParam("page", 1)
                .queryParam("pageSize", -5)
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

    @Test
    void getCardsByInvalidColor_Failure() {
        given()
                .queryParam("colors", "@InvalidColor123")
                .when()
                .get("/cards")
                .then()
                .statusCode(200);
    }

}