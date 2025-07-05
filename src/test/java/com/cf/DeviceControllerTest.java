package com.cf;

import com.cf.core.HttpServerApp;
import com.cf.dto.DeviceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeviceControllerTest {
    private static final String BASE_URL = "http://localhost:8080/api/devices";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public void startServer() {
        HttpServerApp.start();
    }

    @AfterAll
    public void stopServer() {
        HttpServerApp.stop();
    }

    // Helper method to create a device via POST request
    private DeviceDto createDevice(String name, String type, String status) throws IOException {
        DeviceDto dto = new DeviceDto();
        dto.name = name;
        dto.type = type;
        dto.status = status;

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = objectMapper.writeValueAsString(dto);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(201, conn.getResponseCode());

        try (InputStream is = conn.getInputStream()) {
            return objectMapper.readValue(is, DeviceDto.class);
        }
    }

    @Test
    // Test creating a device and then fetching it by ID
    public void testCreateAndGetDevice() throws IOException {
        DeviceDto created = createDevice("Sensor A", "Sensor", "active");
        assertNotNull(created.id);
        assertEquals("Sensor A", created.name);

        // GET by ID
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + created.id).openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());
        DeviceDto fetched = objectMapper.readValue(conn.getInputStream(), DeviceDto.class);
        assertEquals(created.id, fetched.id);
        assertEquals("Sensor A", fetched.name);
    }

    @Test
    // Test fetching all devices, expects at least one device in the list
    public void testGetAllDevices() throws IOException {
        // Ensure at least one device exists
        createDevice("Sensor B", "Sensor", "active");

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());

        DeviceDto[] devices = objectMapper.readValue(conn.getInputStream(), DeviceDto[].class);
        assertTrue(devices.length > 0);
    }

    @Test
    // Test updating an existing device and verifying the update
    public void testUpdateDevice() throws IOException {
        DeviceDto created = createDevice("Sensor C", "Sensor", "active");

        created.name = "Updated Sensor C";
        created.status = "inactive";

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + created.id).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = objectMapper.writeValueAsString(created);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(200, conn.getResponseCode());

        DeviceDto updated = objectMapper.readValue(conn.getInputStream(), DeviceDto.class);
        assertEquals("Updated Sensor C", updated.name);
        assertEquals("inactive", updated.status);
    }

    @Test
    // Test deleting an existing device and confirming it's deleted
    public void testDeleteDevice() throws IOException {
        DeviceDto created = createDevice("Sensor D", "Sensor", "active");

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + created.id).openConnection();
        conn.setRequestMethod("DELETE");

        assertEquals(204, conn.getResponseCode());

        // Confirm deletion: GET should return 404
        HttpURLConnection conn2 = (HttpURLConnection) new URL(BASE_URL + "/" + created.id).openConnection();
        conn2.setRequestMethod("GET");
        assertEquals(404, conn2.getResponseCode());
    }

    @Test
    // Test fetching a device with a random non-existent ID
    public void testGetNonExistingDevice() throws IOException {
        String randomId = UUID.randomUUID().toString();
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + randomId).openConnection();
        conn.setRequestMethod("GET");

        assertEquals(404, conn.getResponseCode());
    }

    @Test
    // Test updating a non-existent device
    public void testUpdateNonExistingDevice() throws IOException {
        String randomId = UUID.randomUUID().toString();

        DeviceDto dto = new DeviceDto();
        dto.id = randomId;
        dto.name = "NonExistent";
        dto.type = "Sensor";
        dto.status = "active";

        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + randomId).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = objectMapper.writeValueAsString(dto);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }

        assertEquals(404, conn.getResponseCode());
    }
}
