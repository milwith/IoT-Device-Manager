package com.cf.core;

import com.cf.controller.DeviceController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerApp {
    private static HttpServer server;

    // Starts the HTTP server and maps URL paths to the DeviceController
    public static void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/devices", new DeviceController());
            server.createContext("/api/devices/", new DeviceController());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started on http://localhost:8080/api/devices");
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Stops the HTTP server if it is running(use for test)
    public static void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped.");
        }
    }
}
