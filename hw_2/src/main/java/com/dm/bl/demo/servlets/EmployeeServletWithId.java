package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.handlers.ParsePath;
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

@WebServlet(
        urlPatterns = {"/employees/*"}
)
public class EmployeeServletWithId extends HttpServlet {
    private final Service<Employee, Long> employeeService = new EmployeeServiceImpl(new JDBCRepositoryEmployee(new DataSourceProvider().getDataSource()));
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            Employee employee = employeeService.read(id);
            String json = JsonConverter.toJson(employee);

            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving employee: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            employeeService.delete(id);
            String message = "Employee with ID " + id + " has been deleted successfully";
            responseHandler.handle(resp, message, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error deleting employee: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
