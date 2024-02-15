package com.dm.bl.demo.service.impl;

import com.dm.bl.demo.dto.DepartmentDto;
import com.dm.bl.demo.entity.Department;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.mapper.DepartmentConverter;
import com.dm.bl.demo.repository.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {
    @Mock
    private Repository<Department, Long> repositoryDepartment;
    @Mock
    private DepartmentConverter converter;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private  Department department = new Department(1L, "department");
    private  DepartmentDto departmentDto = new DepartmentDto(1L, "department");

    @Test
    void create_shouldReturnDto() {
        when(converter.entityToDto(department)).thenReturn(departmentDto);
        when(converter.dtoToEntity(departmentDto)).thenReturn(department);
        when(repositoryDepartment.save(department)).thenReturn(Optional.of(department));

        DepartmentDto expected = departmentService.create(departmentDto);

        assertEquals(expected, departmentDto);
    }

    @Test
    void read_shouldReturnDto_whenExist() {
        Long id = 1L;
        when(repositoryDepartment.getById(id)).thenReturn(Optional.of(department));
        when(converter.entityToDto(department)).thenReturn(departmentDto);

        DepartmentDto expected = departmentService.read(id);

        assertEquals(expected, departmentDto);
    }

    @Test
    void read_shouldThrowException_whenNoExist() {
        assertThrows(NotFoundEntity.class, () -> departmentService.read(2L));
    }

    @Test
    void update_shouldReturnDto_whenExist() {
        DepartmentDto updateDto = new DepartmentDto(1L, "update");
        Department update = new Department(1L, "update");
        when(converter.dtoToEntity(updateDto)).thenReturn(update);
        when(converter.entityToDto(update)).thenReturn(updateDto);
        when(repositoryDepartment.update(update)).thenReturn(Optional.of(update));

        DepartmentDto expected = departmentService.update(updateDto);

        assertEquals(expected, updateDto);
    }

    @Test
    void update_shouldThrowException_whenNoExist() {
        DepartmentDto badDto = new DepartmentDto(2L, "bad");

        assertThrows(NotFoundEntity.class, () -> departmentService.update(badDto));
    }

    @Test
    void delete_shouldDelete() {
        departmentService.delete(1L);

        verify(repositoryDepartment, times(1)).delete(1L);
    }
}