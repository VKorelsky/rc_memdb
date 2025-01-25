package com.memdb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApplicationTest {
    private static final String GET_URL = "http://localhost:4000/get";
    private static final String SET_URL = "http://localhost:4000/set";

    private static Javalin app;

    @BeforeAll
    static void setup() {
        app = Application.startApp();
    }

    @AfterAll
    static void teardown() {
        app.stop();
    }

    @Test
    void testSetAndGetValue() {
        String key = "somekey", value = "somevalue";

        // SET
        HttpResponse<String> setResponse = Unirest.get(SET_URL)
                                                  .queryString(key, value)
                                                  .asString();

        assertEquals(200, setResponse.getStatus());
        assertEquals(value, setResponse.getBody());

        // GET
        HttpResponse<String> getResponse = Unirest.get(GET_URL)
                                                  .queryString("key", key)
                                                  .asString();

        assertEquals(200, getResponse.getStatus());
        assertEquals(value, getResponse.getBody());
    }

    @Test
    void testGetEmptyParams() {
        HttpResponse<String> getResponse = Unirest.get(GET_URL)
                                                  .asString();

        assertEquals(400, getResponse.getStatus());
        assertEquals("bad request", getResponse.getBody());
    }

    @Test
    void testSetMalformedParams() {
        HttpResponse<String> setResponse = Unirest.get(SET_URL + "?param")
                                                  .asString();

        assertEquals(400, setResponse.getStatus());
        assertEquals("bad request", setResponse.getBody());
    }

    @Test
    void testSetMultipleParams() {
        HttpResponse<String> setResponse = Unirest.get(SET_URL)
                                                  .queryString(Map.of(
                                                    "key1", "value1",
                                                    "key2", "value2"
                                                  ))
                                                  .asString();

        assertEquals(400, setResponse.getStatus());
        assertEquals("bad request", setResponse.getBody());
    }
}
