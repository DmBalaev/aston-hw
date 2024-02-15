package com.dm.bl.demo.integration;

import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.integration.AbstractIntegrationTest;
import com.dm.bl.demo.repository.JDBCRepositoryEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeRepositoryIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private DataSource dataSource;
    public JDBCRepositoryEmployee jdbcRepositoryEmployee;

    @BeforeEach
    public void init() {
        jdbcRepositoryEmployee = new JDBCRepositoryEmployee(dataSource);
    }

    @BeforeEach
    public void clear() {
        jdbcRepositoryEmployee.clearAll();
    }

    @Test
    public void save_shouldSaveEmployeeAndReturn() {
        Employee actual = new Employee("Vasya");
        Employee expected = jdbcRepositoryEmployee.save(actual).get();

        assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldGetAllEmployees() {
        Employee insertEntity = new Employee("Vasya");
        jdbcRepositoryEmployee.save(insertEntity).get();

        List<Employee> departments = jdbcRepositoryEmployee.findAll();

        assertEquals(departments.size(), 1);
    }

    @Test
    public void findById_shouldGetEmployee_whenExists() {
        Employee actual = new Employee("Vasya");
        jdbcRepositoryEmployee.save(actual);

        Employee expected = jdbcRepositoryEmployee.getById(2L).orElse(new Employee());

        assertEquals(expected, actual);
    }

    @Test
    public void findById_shouldGetEmptyOptional_whenNotExists() {
        Optional<Employee> expected = jdbcRepositoryEmployee.getById(2L);

        assertEquals(expected, Optional.empty());
    }

    @Test
    public void update_shouldUpdateAndReturnUpdatedEmployee_whenExist() {
        jdbcRepositoryEmployee.save(new Employee("Vasya"));

        Employee update = new Employee(3L, "updateVasya");

        Employee updatedEmployee = jdbcRepositoryEmployee.update(update).orElse(new Employee());

        assertEquals(updatedEmployee, update);
    }

    @Test
    public void update_shouldReturnEmptyOptional_whenNotExist() {
        Employee update = new Employee(2L, "updateVasya");

        Optional<Employee> updatedDepartment = jdbcRepositoryEmployee.update(update);

        assertEquals(updatedDepartment, Optional.empty());
    }

    @Test
    public void delete_shouldReturnEmptyOptional_whenNotExist() {
        Optional<Employee> employee = jdbcRepositoryEmployee.save(new Employee("IT"));

        jdbcRepositoryEmployee.delete(employee.get().getId());

        assertEquals(jdbcRepositoryEmployee.findAll().size(), 0);
    }
}