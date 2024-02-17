package com.dm.bl.demo.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponseHandler {

    public void handle(HttpServletResponse resp, Object data, int status) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper(); // лучше вынести маппер в поле, зачем каждый раз его создавать?
        String json;

        if (data instanceof String){
            json = (String) data;
        }else {
            json = mapper.writeValueAsString(data);
        }

        resp.getWriter().write(json);
        resp.setStatus(status);
    }
}