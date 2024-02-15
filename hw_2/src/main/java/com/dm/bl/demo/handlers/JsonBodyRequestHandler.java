package com.dm.bl.demo.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonBodyRequestHandler<T> {
    private final Class<T> clazz;

    public JsonBodyRequestHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T handle(HttpServletRequest req) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestBody.toString(), clazz);
    }
}