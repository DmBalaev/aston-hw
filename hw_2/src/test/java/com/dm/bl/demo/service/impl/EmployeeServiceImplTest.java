package com.dm.bl.demo.service.impl;

import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.repository.JDBCRepositoryEmployee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private JDBCRepositoryEmployee employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private Employee employee = new Employee(1L, "Vasya");


    @Test
    void create_shouldReturnEmployee() {
        when(employeeRepository.save(employee)).thenReturn(Optional.of(employee));

        Employee expected = employeeService.create(employee);

        assertEquals(expected, employee);
    }

    @Test
    void read_shouldReturnEmployee_whenExist() {
        Long id = 1L;
        when(employeeRepository.getById(id)).thenReturn(Optional.of(employee));

        Employee expected = employeeService.read(id);

        assertEquals(expected, employee);
    }

    @Test
    void read_shouldThrowException_whenNoExist() {
        assertThrows(NotFoundEntity.class, () -> employeeService.read(2L));
    }

    @Test
    void update_shouldReturnEmployee_whenExist() {
        Employee update = new Employee(1L, "Vasya");
        when(employeeRepository.update(update)).thenReturn(Optional.of(update));

        Employee expected = employeeService.update(update);

        assertEquals(expected, update);
    }

    @Test
    void update_shouldThrowException_whenNoExist() {
        Employee badDto = new Employee(2L, "bad");

        assertThrows(NotFoundEntity.class, () -> employeeService.update(badDto));
    }

    @Test
    void delete_shouldDelete() {
        employeeService.delete(1L);

        verify(employeeRepository, times(1)).delete(1L);
    }
}