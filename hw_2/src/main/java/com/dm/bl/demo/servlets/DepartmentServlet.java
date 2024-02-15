package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.dto.DepartmentDto;
import com.dm.bl.demo.handlers.JsonBodyRequestHandler;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.mapper.DepartmentConverter;
import com.dm.bl.demo.mapper.JsonConverter;
import com.dm.bl.demo.repository.JDBCRepositoryDepartment;
import com.dm.bl.demo.service.Service;
import com.dm.bl.demo.service.impl.DepartmentServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(
        urlPatterns = {"/departments"}
)

public class DepartmentServlet extends HttpServlet {
    private final Service<DepartmentDto, Long> service = new DepartmentServiceImpl(new JDBCRepositoryDepartment(new DataSourceProvider().getDataSource()), new DepartmentConverter());
    private final JsonBodyRequestHandler<DepartmentDto> requestBodyHandler = new JsonBodyRequestHandler<>(DepartmentDto.class);
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            DepartmentDto requestDto = requestBodyHandler.handle(req);
            DepartmentDto department = service.create(requestDto);
            responseHandler.handle(resp, department, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            String message = "Error creating department: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            List<DepartmentDto> departments = service.readAll();
            String json = JsonConverter.toJson(departments);

            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving departments: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            DepartmentDto requestDto = requestBodyHandler.handle(req);
            DepartmentDto updatedDepartment = service.update(requestDto);
            responseHandler.handle(resp, updatedDepartment, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error updating department: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}