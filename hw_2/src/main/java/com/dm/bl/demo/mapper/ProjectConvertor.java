package com.dm.bl.demo.mapper;


import com.dm.bl.demo.dto.ProjectDto;
import com.dm.bl.demo.entity.Project;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectConvertor implements Converter<Project, ProjectDto> {
    private final ModelMapper mapper = new ModelMapper();
    @Override
    public ProjectDto entityToDto(Project project) {
        return mapper.map(project, ProjectDto.class);
    }

    @Override
    public Project dtoToEntity(ProjectDto projectDto) {
        return mapper.map(projectDto, Project.class);
    }

    @Override
    public List<ProjectDto> entitiesToDtos(List<Project> projects) {
        return projects.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}