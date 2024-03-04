package com.tabletop.endpoint;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.h2.util.MathUtils.randomInt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import com.tabletop.persistence.model.TTCharacter;

public class CharacterControllerTest {

    private static final String ENDPOINT = "/api/characters";

    private TTCharacter createRandomCharacter() {
        TTCharacter character = new TTCharacter();
        character.setName(randomAlphabetic(6));
        character.setLevel(randomInt(100));

        return character;
    }

    private String createCharacterAsUri(TTCharacter character) {
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(character)
            .post(ENDPOINT);

        return ENDPOINT + "/" + response.jsonPath().get("id");
    }

    private void assertRetrievedCharacterIsValid(TTCharacter character, Response response) {
        assertEquals(character.getId(), response.jsonPath().get("id"));
        assertEquals(character.getName(), response.jsonPath().get("name"));
        assertEquals(character.getLevel(), response.jsonPath().get("level"));
    }

    @Test
    public void whenGetAllCharacters_thenOK() {
        Response response = RestAssured.get(ENDPOINT);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetCharacterById_thenOK() {
        TTCharacter character = createRandomCharacter();
        String uri = createCharacterAsUri(character);
        Response response = RestAssured.get(uri);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertRetrievedCharacterIsValid(character, response);
    }

    @Test
    public void whenGetCharacterById_thenNotFound() {
        Response response = RestAssured.get(ENDPOINT + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void whenGetCharacterByName_thenOk() {
        TTCharacter character = createRandomCharacter();
        createCharacterAsUri(character);

        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("name", character.getName())
            .post(ENDPOINT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertRetrievedCharacterIsValid(character, response);
    }

    @Test
    public void whenCreateNewCharacter_thenCreated() {
        TTCharacter character = createRandomCharacter();
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(character)
            .post(ENDPOINT);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidCharacter_thenError() {
        TTCharacter character = createRandomCharacter();
        character.setName(null);
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(character)
            .post(ENDPOINT);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedCharacter_thenUpdated() {
        TTCharacter character = createRandomCharacter();
        String uri = createCharacterAsUri(character);
        character.setId(Long.parseLong(uri.split(ENDPOINT + "/")[1]));
        character.setName(randomAlphabetic(6));
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(character)
            .put(uri);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(uri);
        assertRetrievedCharacterIsValid(character, response);
    }

    @Test
    public void whenDeleteCreatedCharacter_thenOk() {
        TTCharacter character = createRandomCharacter();
        String uri = createCharacterAsUri(character);
        Response response = RestAssured.delete(uri);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(uri);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
