package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.dto.ProjectDto;
import com.dm.bl.demo.handlers.JsonResponseHandler;
import com.dm.bl.demo.handlers.ParsePath;
import com.dm.bl.demo.mapper.JsonConverter;
import com.dm.bl.demo.mapper.ProjectConvertor;
import com.dm.bl.demo.repository.JDBCRepositoryProject;
import com.dm.bl.demo.service.Service;
import com.dm.bl.demo.service.impl.ProjectServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        urlPatterns = {"/projects/*"}
)
public class ProjectServetWitId extends HttpServlet {
    private final Service<ProjectDto, Long> projectService = new ProjectServiceImpl(new JDBCRepositoryProject(new DataSourceProvider().getDataSource()), new ProjectConvertor());
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            ProjectDto project = projectService.read(id);
            String json = JsonConverter.toJson(project);

            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving project: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = ParsePath.getIdFromPath(req);
        try {
            projectService.delete(id);
            String message = "Project with ID " + id + " has been deleted successfully";
            responseHandler.handle(resp, message, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error deleting project: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
