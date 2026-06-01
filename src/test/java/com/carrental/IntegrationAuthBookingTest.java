package com.carrental;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationAuthBookingTest {

    // FIXED: Using the official Spring Boot 3 test annotation
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerLoginAndBook() throws Exception {
        // FIXED: Removed the invalid /v1 path that was causing 404 Not Found errors
        String base = "http://localhost:" + port + "/api";

        // 1. Register User
        Map<String, Object> register = Map.of(
                "fullName", "IT Test User",
                "email", "it.user+1@example.com",
                "phone", "9000000001",
                "password", "P@ssw0rd"
        );

        ResponseEntity<String> regResp = restTemplate.postForEntity(base + "/auth/register", register, String.class);
        assertThat(regResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 2. Login User
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

        // 3. Get Available Cars
        // FIXED: Pointing to the correct public endpoint based on SecurityConfig
        ResponseEntity<String> carsResp = restTemplate.getForEntity(base + "/cars/available", String.class);
        assertThat(carsResp.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode carsJson = objectMapper.readTree(carsResp.getBody());
        JsonNode carsArray = carsJson.path("data").path("content");

        // Handle variations in pagination response wrappers dynamically
        if (carsArray.isMissingNode() || !carsArray.isArray()) {
            carsArray = carsJson.path("content");
        }
        if (carsArray.isMissingNode() || !carsArray.isArray()) {
            carsArray = carsJson.path("data");
        }
        if (carsArray.isMissingNode() || !carsArray.isArray()) {
            carsArray = carsJson;
        }

        assertThat(carsArray.isArray()).isTrue();
        assertThat(carsArray.size()).isGreaterThan(0);

        long carId = carsArray.get(0).path("id").asLong();

        // 4. Create Booking
        LocalDateTime start = LocalDateTime.now().plusDays(2).withSecond(0).withNano(0);
        LocalDateTime end = start.plusDays(2);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        Map<String, Object> booking = Map.of(
                "carId", carId,
                "startAt", start.format(fmt), // Correctly matching new DTO structure
                "endAt", end.format(fmt)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(booking, headers);

        ResponseEntity<String> bookResp = restTemplate.postForEntity(base + "/bookings", req, String.class);

        assertThat(bookResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        JsonNode bookJson = objectMapper.readTree(bookResp.getBody());
        assertThat(bookJson.path("success").asBoolean()).isTrue();
        assertThat(bookJson.path("data").path("id").isNumber()).isTrue();
    }
}