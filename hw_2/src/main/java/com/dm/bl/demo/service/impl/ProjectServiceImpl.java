package com.dm.bl.demo.service.impl;


import com.dm.bl.demo.dto.ProjectDto;
import com.dm.bl.demo.entity.Project;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.mapper.ProjectConvertor;
import com.dm.bl.demo.repository.Repository;
import com.dm.bl.demo.service.Service;

import java.util.List;

public class ProjectServiceImpl implements Service<ProjectDto, Long> {
    private final Repository<Project, Long> repositoryProject;
    private final ProjectConvertor converter;

    public ProjectServiceImpl(Repository<Project, Long> repositoryProject, ProjectConvertor converter) {
        this.repositoryProject = repositoryProject;
        this.converter = converter;
    }

    @Override
    public ProjectDto create(ProjectDto request) {
        Project project = converter.dtoToEntity(request);
        Project savedProject = repositoryProject.save(project).get();

        return converter.entityToDto(savedProject);
    }

    @Override
    public ProjectDto read(Long id) {
        Project project = repositoryProject.getById(id)
                .orElseThrow(()-> new NotFoundEntity("Project not found."));

        return converter.entityToDto(project);
    }

    @Override
    public ProjectDto update(ProjectDto request) {
        Project project = converter.dtoToEntity(request);
        Project updatedProject = repositoryProject.update(project)
                .orElseThrow(()-> new NotFoundEntity("Project not found."));

        return converter.entityToDto(updatedProject);
    }

    @Override
    public void delete(Long id) {
        repositoryProject.delete(id);
    }

    @Override
    public List<ProjectDto> readAll() {
        return converter.entitiesToDtos(repositoryProject.findAll());
    }
}
