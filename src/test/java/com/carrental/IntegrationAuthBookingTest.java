package com.carrental;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationAuthBookingTest {

    @com.carrental.LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerLoginAndBook() throws Exception {
        String base = "http://localhost:" + port + "/api/v1";

        // register
        Map<String, Object> register = Map.of(
                "fullName", "IT Test User",
                "email", "it.user+1@example.com",
                "phone", "9000000001",
                "password", "P@ssw0rd"
        );

        ResponseEntity<String> regResp = restTemplate.postForEntity(base + "/auth/register", register, String.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // login
        Map<String, Object> login = Map.of(
                "email", "it.user+1@example.com",
                "password", "P@ssw0rd"
        );

        ResponseEntity<String> loginResp = restTemplate.postForEntity(base + "/auth/login", login, String.class);
        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode loginJson = objectMapper.readTree(loginResp.getBody());
        String token = null;
        if (loginJson.has("data") && loginJson.get("data").has("accessToken")) {
            token = loginJson.get("data").get("accessToken").asText();
        } else if (loginJson.has("accessToken")) {
            token = loginJson.get("accessToken").asText();
        }
        assertThat(token).isNotNull();

        // get cars
        ResponseEntity<String> carsResp = restTemplate.getForEntity(base + "/cars", String.class);
        assertThat(carsResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode carsJson = objectMapper.readTree(carsResp.getBody());
        JsonNode carsArray = carsJson.path("data").path("content");
        if (!carsArray.isArray() || carsArray.size() == 0) {
            // try alternative shapes
            carsArray = carsJson.path("content");
        }
        assertThat(carsArray.isArray()).isTrue();
        assertThat(carsArray.size()).isGreaterThan(0);

        long carId = carsArray.get(0).path("id").asLong();

        // prepare booking dates: future
        LocalDateTime start = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
        LocalDateTime end = start.plusDays(2);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        Map<String, Object> booking = Map.of(
                "carId", carId,
                "startAt", start.format(fmt),
                "endAt", end.format(fmt)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(booking, headers);

        ResponseEntity<String> bookResp = restTemplate.postForEntity(base + "/bookings", req, String.class);

        // Should be 201 CREATED
        assertThat(bookResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JsonNode bookJson = objectMapper.readTree(bookResp.getBody());
        assertThat(bookJson.path("success").asBoolean()).isTrue();
        assertThat(bookJson.path("data").path("id").isNumber()).isTrue();
    }
}

