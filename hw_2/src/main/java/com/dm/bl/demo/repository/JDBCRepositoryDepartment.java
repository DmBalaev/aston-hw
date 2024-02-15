package com.dm.bl.demo.repository;

import com.dm.bl.demo.entity.Department;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JDBCRepositoryDepartment implements Repository<Department, Long> {
    private final String INSERT = "INSERT INTO department (name) VALUES (?)";
    private final String SELECT_ALL = "SELECT * FROM department";
    private final String SELECT_BY_ID = "SELECT * FROM department WHERE id = ?";
    private final String UPDATE = "UPDATE department SET name = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM department WHERE id = ?";
    private final String DELETE_ALL = "DELETE FROM department";
    private final DataSource dataSource;

    public JDBCRepositoryDepartment(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Department> save(Department entity) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    log.info("Saved department with id: {}", id);
                    return Optional.of(entity);
                } else {
                    log.error("Failed to retrieve generated id for department.");
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Error saving department: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong("id"));
                department.setName(resultSet.getString("name"));

                departments.add(department);
            }
        } catch (SQLException e) {
            log.error("Error finding all departments: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return departments;
    }

    @Override
    public Optional<Department> getById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong("id"));
                department.setName(resultSet.getString("name"));
                return Optional.of(department);
            } else {
                log.warn("No department found with id {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error retrieving department with {}: {} ", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Department> update(Department entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                log.warn("No department found with id {} to update", entity.getId());
                return Optional.empty();
            } else {
                log.info("Department with id {} updated successfully", entity.getId());
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            log.error("Error updating department with id {}: {}", entity.getId(), e.getMessage());
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
                log.warn("No department with id {} found to delete", id);
            } else {
                log.info("Department with id {} deleted successfully", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting department with id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            log.info("{} rows deleted from department table", rowsDeleted);
        } catch (SQLException e) {
            log.error("Error clearing department table: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}