package com.cf.controller;

import com.cf.dto.DeviceDto;
import com.cf.model.Device;
import com.cf.service.DeviceService;
import com.cf.service.serviceImpl.DeviceServiceImpl;
import com.cf.util.DeviceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DeviceController implements HttpHandler {

    private final DeviceService service = new DeviceServiceImpl();
    private final ObjectMapper objectMapper;


    public DeviceController() {
        //convert date time format readable
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat());
    }

    //Main entry point for handling HTTP requests
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();
            String method = exchange.getRequestMethod();
            String[] parts = path.split("/");

                 // GET /api/devices - List all devices
            if (path.equals("/api/devices") && method.equals("GET")) {
                List<DeviceDto> devices = service.getAllDevices().stream()
                        .map(DeviceMapper::toDto).collect(Collectors.toList());
                respond(exchange, 200, devices);
                // POST /api/devices - Create new device
            } else if (path.equals("/api/devices") && method.equals("POST")) {
                DeviceDto dto = readRequestBody(exchange, DeviceDto.class);
                Device created = service.createDevice(DeviceMapper.toEntity(dto));
                respond(exchange, 201, DeviceMapper.toDto(created));
                // Handle /api/devices/{id} for GET, PUT, DELETE
            } else if (parts.length == 4 && parts[2].equals("devices")) {
                String id = parts[3];

                switch (method) {
                    case "GET" -> {
                        Optional<Device> found = service.getDevice(id);
                        if (found.isPresent()) {
                            respond(exchange, 200, DeviceMapper.toDto(found.get()));
                        } else {
                            respond(exchange, 404, "Device not found");
                        }
                    }
                    case "PUT" -> {
                        DeviceDto dto = readRequestBody(exchange, DeviceDto.class);
                        Optional<Device> updated = service.updateDevice(id, DeviceMapper.toEntity(dto));
                        if (updated.isPresent()) {
                            respond(exchange, 200, DeviceMapper.toDto(updated.get()));
                        } else {
                            respond(exchange, 404, "Device not found");
                        }
                    }
                    case "DELETE" -> {
                        boolean deleted = service.deleteDevice(id);
                        respond(exchange, deleted ? 204 : 404, "Deleted Successfully");
                    }
                    default -> respond(exchange, 405, "Method Not Allowed");
                }
            } else {
                respond(exchange, 404, "Invalid endpoint");
            }

        } catch (Exception e) {
            respond(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    //Helper method to read JSON request body and convert to Java object
    private <T> T readRequestBody(HttpExchange exchange, Class<T> clazz) throws IOException {
        return objectMapper.readValue(exchange.getRequestBody(), clazz);
    }

    //Helper method to write response as JSON
    private void respond(HttpExchange exchange, int statusCode, Object body) throws IOException {
        byte[] response = body == null
                ? new byte[0]
                : objectMapper.writeValueAsBytes(body);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}

