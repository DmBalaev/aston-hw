package com.dm.bl.demo.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ParsePath {

    public static Long getIdFromPath(HttpServletRequest req) throws ServletException{
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.isEmpty()) {
            throw new ServletException("ID parameter is required");
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length < 2) {
            throw new ServletException("Invalid path format");
        }

        try {
            return Long.parseLong(pathParts[pathParts.length - 1]);
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid id format", e);
        }
    }
}