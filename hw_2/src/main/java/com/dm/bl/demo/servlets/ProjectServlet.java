package com.dm.bl.demo.servlets;


import com.dm.bl.demo.config.DataSourceProvider;
import com.dm.bl.demo.dto.ProjectDto;
import com.dm.bl.demo.handlers.JsonBodyRequestHandler;
import com.dm.bl.demo.handlers.JsonResponseHandler;
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
import java.util.List;

@WebServlet(
        urlPatterns = {"/projects"}
)
public class ProjectServlet extends HttpServlet {
    private final Service<ProjectDto, Long> projectService = new ProjectServiceImpl(new JDBCRepositoryProject(new DataSourceProvider().getDataSource()), new ProjectConvertor());
    private final JsonBodyRequestHandler<ProjectDto> requestBodyHandler = new JsonBodyRequestHandler<>(ProjectDto.class);
    private final JsonResponseHandler responseHandler = new JsonResponseHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ProjectDto> projects = projectService.readAll();
            String json = JsonConverter.toJson(projects);

            responseHandler.handle(resp, json, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error retrieving projects: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ProjectDto requestDto = requestBodyHandler.handle(req);
            ProjectDto project = projectService.create(requestDto);
            responseHandler.handle(resp, project, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            String message = "Error creating project: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            ProjectDto requestDto = requestBodyHandler.handle(req);
            ProjectDto updatedProject = projectService.update(requestDto);
            responseHandler.handle(resp, updatedProject, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            String message = "Error updating project: " + e.getMessage();
            responseHandler.handle(resp, message, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}