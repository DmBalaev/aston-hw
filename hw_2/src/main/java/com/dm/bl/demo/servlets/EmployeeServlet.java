package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.handlers.JsonBodyRequestHandler;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.mapper.JsonConverter;
import com.dm.bl.demo.repository.JDBCRepositoryEmployee;
import com.dm.bl.demo.service.Service;
import com.dm.bl.demo.service.impl.EmployeeServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(
        urlPatterns = {"/employees"}
)
public class EmployeeServlet extends HttpServlet {
    private final Service<Employee, Long> employeeService = new EmployeeServiceImpl(new JDBCRepositoryEmployee(new DataSourceProvider().getDataSource()));
    private final JsonBodyRequestHandler<Employee> requestBodyHandler = new JsonBodyRequestHandler<>(Employee.class);
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Employee> employees = employeeService.readAll();
            String json = JsonConverter.toJson(employees);
            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving employees: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            Employee requestDto = requestBodyHandler.handle(req);
            Employee employee = employeeService.create(requestDto);
            responseHandler.handle(resp, employee,HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            String message = "Error creating employee: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Employee requestDto = requestBodyHandler.handle(req);
            Employee updatedEmployee = employeeService.update(requestDto);
            responseHandler.handle(resp, updatedEmployee, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error updating employee: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}