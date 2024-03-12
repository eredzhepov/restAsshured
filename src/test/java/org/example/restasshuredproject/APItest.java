package org.example.restasshuredproject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APItest {
    private final int unexistingPetId = 1123;

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
    @Test
    public void newPetTest(){
        Integer id = 1123;
        String name = "Wale";
        String status = "pending";
        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        request.put("name", name);
        request.put("status", status);
        given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "pet/")
                .then().log().all()
                .assertThat()
                .time(lessThan(3000L))
                .statusCode(200)
                .body("id",equalTo(id), "name",equalTo(name), "status", equalTo(status));

    }
    @Test
    @DisplayName("Размещение заказа на приобретение ")
    public void petPlaceOrder(){
        Integer id = 1123;
        Integer petId = 0;
        Integer quantity = 0;
        String shipDate = "2024-03-12T05:44:25.955Z";
        String status = "placed";
        Boolean complete = true;
        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        request.put("petID", petId.toString());
        request.put(" ", "0");
        request.put("shipDate", shipDate);
        request.put("status", status);
        request.put("complete", complete.toString());
        given().contentType("application/json")
                .body(request)
                .when()
                .post(baseURI + "store/order/")
                .then().log().all()
                .assertThat()
                .time(lessThan(3000L))
                .statusCode(200)
                .body("id",equalTo(id), "status",equalTo(status), "complete", equalTo(complete));
    }
    @Test
    @DisplayName("Удаление питомца")
    public void findCreatedPet(){
        Integer id = 1123;
        Map<String, String> request = new HashMap<>();
        request.put("id", id.toString());
        given().contentType("application/json")
                .body(request)
                .when()
                .delete(baseURI + "pet/"+id)
                .then().log().all()
                .assertThat()
                .time(lessThan(3000L))
                .statusCode(200)
                .body("code", equalTo(200));
    }
}
