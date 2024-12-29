package com.example.sendvideo.service;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleHttpServer {
    private static final int PORT = 8080;
    private static final String BASE_DIR = "";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new HttpHandler());
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    static class HttpHandler implements com.sun.net.httpserver.HttpHandler {
        @Override
        public void handle(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String filePath = BASE_DIR + exchange.getRequestURI().getPath().substring(1);
                Path path = Paths.get(filePath);
                if (Files.exists(path) && !Files.isDirectory(path)) {
                    byte[] fileBytes = Files.readAllBytes(path);
                    exchange.sendResponseHeaders(200, fileBytes.length);
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(fileBytes);
                    responseBody.close();
                } else {
                    String response = "File not found";
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream responseBody = exchange.getResponseBody();
                    responseBody.write(response.getBytes());
                    responseBody.close();
                }
            }
        }
    }
}
