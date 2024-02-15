package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.dto.DepartmentDto;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.handlers.ParsePath;
import com.dm.bl.demo.mapper.DepartmentConverter;
import com.dm.bl.demo.mapper.JsonConverter;
import com.dm.bl.demo.repository.JDBCRepositoryDepartment;
import com.dm.bl.demo.service.Service;
import com.dm.bl.demo.service.impl.DepartmentServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        urlPatterns = {"/departments/*"}
)
public class DepartmentServletWithId extends HttpServlet {
    private final Service<DepartmentDto, Long> service = new DepartmentServiceImpl(new JDBCRepositoryDepartment(new DataSourceProvider().getDataSource()), new DepartmentConverter());
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            DepartmentDto department = service.read(id);
            String json = JsonConverter.toJson(department);

            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving departments: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            service.delete(id);
            String message = "Department with ID " + id + " has been deleted successfully";
            responseHandler.handle(resp, message, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error deleting department: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
