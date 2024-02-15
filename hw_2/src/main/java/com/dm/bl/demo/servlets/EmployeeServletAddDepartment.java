package com.dm.bl.demo.servlets;

import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.dto.ApiResponse;
import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.handlers.ParserPathWithTwoId;
import com.dm.bl.demo.repository.JDBCRepositoryEmployee;
import com.dm.bl.demo.service.Service;
import com.dm.bl.demo.service.impl.EmployeeServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        urlPatterns = {"/employees/addDepartment/*"}
)
public class EmployeeServletAddDepartment extends HttpServlet {
    private final EmployeeServiceImpl employeeService = new EmployeeServiceImpl(new JDBCRepositoryEmployee(new DataSourceProvider().getDataSource()));
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        try {
            long[] ids = ParserPathWithTwoId.getIdFromPath(req);

            Long eId = ids[0];
            Long dId = ids[1];

            ApiResponse response = employeeService.addDepartment(eId, dId);

            responseHandler.handle(resp, response, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error adding department to employee: " + e.getMessage());
        }
    }
}