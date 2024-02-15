package com.dm.bl.demo.service.impl;

import com.dm.bl.demo.dto.ProjectDto;
import com.dm.bl.demo.entity.Project;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.mapper.ProjectConvertor;
import com.dm.bl.demo.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {
    @Mock
    private Repository<Project, Long> repository;
    @Mock
    private ProjectConvertor converter;
    @InjectMocks
    private ProjectServiceImpl departmentService;

    private final Project project = new Project(1L, "Project");
    private final ProjectDto projectDto = new ProjectDto(1L, "Project");

    @Test
    void create_shouldReturnDto() {
        when(converter.entityToDto(project)).thenReturn(projectDto);
        when(converter.dtoToEntity(projectDto)).thenReturn(project);
        when(repository.save(project)).thenReturn(Optional.of(project));

        ProjectDto expected = departmentService.create(projectDto);

        assertEquals(expected, projectDto);
    }

    @Test
    void read_shouldReturnDto_whenExist() {
        Long id = 1L;
        when(repository.getById(id)).thenReturn(Optional.of(project));
        when(converter.entityToDto(project)).thenReturn(projectDto);

        ProjectDto expected = departmentService.read(id);

        assertEquals(expected, projectDto);
    }

    @Test
    void read_shouldThrowException_whenNoExist() {
        assertThrows(NotFoundEntity.class, ()-> departmentService.read(2L));
    }

    @Test
    void update_shouldReturnDto_whenExist() {
        ProjectDto updateDto = new ProjectDto(1L, "update");
        Project update = new Project(1L, "update");
        when(converter.dtoToEntity(updateDto)).thenReturn(update);
        when(converter.entityToDto(update)).thenReturn(updateDto);
        when(repository.update(update)).thenReturn(Optional.of(update));

        ProjectDto expected = departmentService.update(updateDto);

        assertEquals(expected, updateDto);
    }

    @Test
    void update_shouldThrowException_whenNoExist() {
        ProjectDto badDto = new ProjectDto(2L, "bad");

        assertThrows(NotFoundEntity.class, ()-> departmentService.update(badDto));
    }

    @Test
    void delete_shouldDelete() {
        departmentService.delete(1L);

        verify(repository, times(1)).delete(1L);
    }
}