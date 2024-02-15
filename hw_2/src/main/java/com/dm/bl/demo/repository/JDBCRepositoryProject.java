package com.dm.bl.demo.repository;

import com.dm.bl.demo.entity.Project;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JDBCRepositoryProject implements Repository<Project, Long> {
    private final String INSERT = "INSERT INTO project (name) VALUES (?)";
    private final String SELECT_ALL = "SELECT * FROM project";
    private final String SELECT_BY_ID = "SELECT * FROM project WHERE id = ?";
    private final String UPDATE = "UPDATE project SET name = ? WHERE id = ?";
    private final String DELETE_BY_ID = " DELETE FROM project WHERE id = ?";
    private final String DELETE_ALL = "DELETE FROM project";
    private final DataSource dataSource;

    public JDBCRepositoryProject(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Project> save(Project entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    log.info("Saved project with id: {}", id);
                    return Optional.of(entity);
                } else {
                    log.error("Failed to retrieve generated id for project.");
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Error saving project: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> findAll() {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));

                projects.add(project);
            }
        } catch (SQLException e) {
            log.error("Error finding all project: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return projects;
    }

    @Override
    public Optional<Project> getById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));
                return Optional.of(project);
            } else {
                log.warn("No project found with id {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error retrieving project with {}: {} ", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Project> update(Project entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                log.warn("No project found with id {} to update", entity.getId());
                return Optional.empty();
            } else {
                log.info("Project with id {} updated successfully", entity.getId());
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            log.error("Error updating project with id {}: {}", entity.getId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                log.warn("No project with id {} found to delete", id);
            } else {
                log.info("Project with id {} deleted successfully", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting project with id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            log.info("{} rows deleted from project table", rowsDeleted);
        } catch (SQLException e) {
            log.error("Error clearing project table: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}