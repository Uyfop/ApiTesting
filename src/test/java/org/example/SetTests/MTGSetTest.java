package org.example.SetTests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MTGSetTest {


    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://api.magicthegathering.io/v1";
    }

    @Test
    void getAllSets_Success() {
        given()
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("sets", not(empty()));
    }
    @Test
    void getSetById_Success() {
        given()
                .pathParam("Id", "KTK")
                .when()
                .get("/sets/{Id}")
                .then()
                .statusCode(200)
                .body("set.code", equalTo("KTK"))
                .body("set.name", equalTo("Khans of Tarkir"))
                .body("set.block", equalTo("Khans of Tarkir"))
                .body("set.releaseDate", equalTo("2014-09-26"));
    }

    @Test
    void getSetById_NotFound() {
        given()
                .pathParam("Id", "nonexistentset")
                .when()
                .get("/sets/{Id}")
                .then()
                .statusCode(404);
    }

    @Test
    void pagination_Success() {
        given()
                .queryParam("page", 1)
                .queryParam("pageSize", 10)
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets.size()", equalTo(10));
    }

    @Test
    void getSetsByExpansionType_Success() {
        given()
                .queryParam("type", "core")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", not(empty()))
                .body("sets.type", everyItem(equalTo("core")));
    }

    @Test
    void getSetsWithEmptyResult_Failure() {
        given()
                .queryParam("name", "NotExists")
                .when()
                .get("/sets")
                .then()
                .statusCode(200);
    }

    @Test
    void getSetsByBlock_Success() {
        given()
                .queryParam("block", "Khans of Tarkir")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", not(empty()))
                .body("sets.block", everyItem(equalTo("Khans of Tarkir")));
    }
    @Test
    void getSetByInvalidId_NotFound() {
        given()
                .pathParam("Id", "NONEXISTENT")
                .when()
                .get("/sets/{Id}")
                .then()
                .statusCode(404);
    }
    @Test
    void getSetsByName_Success() {
        given()
                .queryParam("name", "Khans of Tarkir")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", not(empty()))
                .body("sets.name", hasItem("Khans of Tarkir"))
                .body("sets.code", hasItem("KTK"));
    }

    @Test
    void getSetsByInvalidName_Failure() {
        given()
                .queryParam("name", "Nonexistent Set")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", hasSize(0));
    }

    @Test
    void getSetsByInvalidBlock_Failure() {
        given()
                .queryParam("block", "Nonexistent Block")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", hasSize(0));
    }

    @Test
    void getSetsByInvalidType_Failure() {
        given()
                .queryParam("type", "Nonexistent Type")
                .when()
                .get("/sets")
                .then()
                .statusCode(200)
                .body("sets", hasSize(0));
    }

    @Test
    void getBoosterPack_Success() {
        given()
                .pathParam("code", "2ED")
                .when()
                .get("/sets/{code}/booster")
                .then()
                .statusCode(200);
    }
    @Test
    void getBoosterPack_NotFound() {
        given()
                .pathParam("code", "GK1")
                .when()
                .get("/sets/{code}/booster")
                .then()
                .statusCode(400);
    }

}
