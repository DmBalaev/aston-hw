package com.dm.bl.demo.integration;

import com.dm.bl.demo.entity.Department;
import com.dm.bl.demo.integration.AbstractIntegrationTest;
import com.dm.bl.demo.repository.JDBCRepositoryDepartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentRepositoryIntegrationTest extends AbstractIntegrationTest {
    public  JDBCRepositoryDepartment repositoryDepartment;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void init(){
        repositoryDepartment = new JDBCRepositoryDepartment(dataSource);
    }

    @BeforeEach
    public void clear(){
        repositoryDepartment.clearAll();
    }

    @Test
    public void save_shouldSaveDepartmentAndReturn() {
        Department actual = new Department("IT");
        Department expected = repositoryDepartment.save(actual).get();

        assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldGetAllDepartments() {
        Department insertEntity = new Department("IT");
        repositoryDepartment.save(insertEntity).get();

        List<Department> departments = repositoryDepartment.findAll();

        assertEquals(departments.size(), 1);
    }

    @Test
    public void findById_shouldGetDepartment_whenExists() {
        Department actual = new Department("IT");
        repositoryDepartment.save(actual);

        Department expected = repositoryDepartment.getById(5L).orElse(new Department());

        assertEquals(expected, actual);
    }

    @Test
    public void findById_shouldGetEmptyOptional_whenNotExists() {
        Optional<Department> expected = repositoryDepartment.getById(2L);

        assertEquals(expected, Optional.empty());
    }

    @Test
    public void update_shouldUpdateAndReturnUpdatedDepartment_whenExist() {
        repositoryDepartment.save(new Department("IT"));
        Department update = new Department(2L, "update");

        Department updatedDepartment = repositoryDepartment.update(update).orElse(new Department());

        assertEquals(updatedDepartment, update);
    }

    @Test
    public void update_shouldReturnEmptyOptional_whenNotExist() {
        Department update = new Department(2L, "update");

        Optional<Department> updatedDepartment = repositoryDepartment.update(update);

        assertEquals(updatedDepartment, Optional.empty());
    }

    @Test
    public void delete_shouldReturnEmptyOptional_whenNotExist() {
        Optional<Department> department = repositoryDepartment.save(new Department("IT"));

        repositoryDepartment.delete(department.get().getId());

        assertEquals(repositoryDepartment.findAll().size(), 0);
    }
}