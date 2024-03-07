package org.example.restasshuredproject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APItest {
    private final int unexistingPetId = 3456;

    @BeforeEach
    public void setUp(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }

    @Test
    public void petNotFoundTestWithRestAsshured(){
        RestAssured.baseURI += "pet/" + unexistingPetId;
        requestSpecification = RestAssured.given();

        Response response = requestSpecification.get();
        System.out.println("Responce " + response.asPrettyString());

        assertEquals(404, response.statusCode(), "Не тот статус code");
        assertEquals("HTTP/1.1 404 Not Found", response.statusLine(), "Некорректная status line" );
        assertEquals("Pet not found", response.jsonPath().get("message"), "Не то сообщение");
    }
    @Test
    public void  petsNotFoundTesdt(){
        RestAssured.baseURI += "pet/" + unexistingPetId;
        requestSpecification = RestAssured.given();

        Response response = requestSpecification.get();
        System.out.println("Responce " + response.asPrettyString());
        ValidatableResponse validatableResponse = response.then();
        validatableResponse.statusCode(404);
        validatableResponse.statusLine("HTTP/1.1 404 Not Found");
        validatableResponse.body("message",equalTo("Pet not found"));
    }
    @Test
    public void petsNotFoundBDDTest(){
        given().when()
                .get(baseURI + "pet/{id}", unexistingPetId)
                .then()
                .log().all()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("type", equalTo("error"),"message",equalTo("Pet not found"));
    }

}
