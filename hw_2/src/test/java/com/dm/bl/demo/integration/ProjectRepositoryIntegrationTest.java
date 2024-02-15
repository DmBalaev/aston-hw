package com.dm.bl.demo.integration;

import com.dm.bl.demo.entity.Project;
import com.dm.bl.demo.integration.AbstractIntegrationTest;
import com.dm.bl.demo.repository.JDBCRepositoryProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectRepositoryIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private DataSource dataSource;
    public JDBCRepositoryProject repositoryProject;

    @BeforeEach
    public void init() {
        repositoryProject = new JDBCRepositoryProject(dataSource);
    }

    @BeforeEach
    public void clear() {
        repositoryProject.clearAll();
    }

    @Test
    public void save_shouldSaveProjectAndReturn() {
        Project actual = new Project("IT");
        Project expected = repositoryProject.save(actual).get();

        assertEquals(expected, actual);
    }

    @Test
    void findAll_shouldGetAllProjects() {
        Project insertEntity = new Project("IT");
        repositoryProject.save(insertEntity).get();

        List<Project> departments = repositoryProject.findAll();

        assertEquals(departments.size(), 1);
    }

    @Test
    public void findById_shouldGetProject_whenExists() {
        Project actual = new Project("IT");
        repositoryProject.save(actual);
        Project expected = repositoryProject.getById(2L).orElse(new Project());

        assertEquals(expected, actual);
    }

    @Test
    public void findById_shouldGetEmptyOptional_whenNotExists() {
        Optional<Project> expected = repositoryProject.getById(2L);

        assertEquals(expected, Optional.empty());
    }

    @Test
    public void update_shouldUpdateAndReturnUpdatedProject_whenExist() {
        repositoryProject.save(new Project("IT"));
        Project update = new Project(4L, "update");

        Project updatedDepartment = repositoryProject.update(update).orElse(new Project());

        assertEquals(updatedDepartment, update);
    }

    @Test
    public void update_shouldReturnEmptyOptional_whenNotExist() {
        Project update = new Project(2L, "update");

        Optional<Project> updatedDepartment = repositoryProject.update(update);

        assertEquals(updatedDepartment, Optional.empty());
    }

    @Test
    public void delete_shouldReturnEmptyOptional_whenNotExist() {
        Optional<Project> department = repositoryProject.save(new Project("IT"));

        repositoryProject.delete(department.get().getId());

        assertEquals(repositoryProject.findAll().size(), 0);
    }
}