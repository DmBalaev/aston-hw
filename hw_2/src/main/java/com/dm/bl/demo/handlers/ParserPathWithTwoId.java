package com.dm.bl.demo.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class ParserPathWithTwoId {
    public static long[] getIdFromPath(HttpServletRequest req) throws ServletException {
        String pathInfo = req.getPathInfo();
        long[] ids = new long[2];

        if (pathInfo == null || pathInfo.isEmpty()) {
            throw new ServletException("ID parameter is required");
        }

        String[] pathParts = pathInfo.split("/");
        if (pathParts.length < 2) {
            throw new ServletException("Invalid path format");
        }

        try {
            ids[0] = Long.parseLong(pathParts[pathParts.length - 3]);
            ids[1] = Long.parseLong(pathParts[pathParts.length - 1]);

            return ids;
        } catch (NumberFormatException e) {
            throw new ServletException("Invalid id format", e);
        }
    }
}