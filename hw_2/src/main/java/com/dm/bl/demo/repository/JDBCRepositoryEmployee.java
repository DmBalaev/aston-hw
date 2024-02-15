package com.dm.bl.demo.repository;


import com.dm.bl.demo.entity.Department;
import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.entity.Project;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
public class JDBCRepositoryEmployee implements Repository<Employee, Long> {
    private final String INSERT = "INSERT INTO employee (name) VALUES (?)";
    private final String SELECT_ALL = "SELECT * FROM employee";
    private final String SELECT_BY_ID = "SELECT * FROM employee WHERE id = ?";
    private final String UPDATE = "UPDATE employee SET name = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM employee WHERE id = ?";
    private final String DELETE_ALL = "DELETE FROM employee";
    private final String SELECT_PROJECTS_BY_ID = "SELECT p.* FROM project p JOIN employee_project ep ON p.id = ep.project_id WHERE ep.employee_id = ?";
    private final String SELECT_DEPARTMENT = "SELECT * FROM department WHERE id = ?";
    private final String INSERT_EMPLOYEE_PROJECT = "INSERT INTO employee_project (employee_id, project_id) VALUES (?, ?)";
    private final String ADD_DEPARTMENT_TO_EMPLOYEE = "UPDATE employee SET department_id = ? WHERE id = ?";
    private final DataSource dataSource;

    public JDBCRepositoryEmployee(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Employee> save(Employee entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    entity.setId(id);
                    log.info("Saved employee with id: {}", id);
                    return Optional.of(entity);
                } else {
                    log.error("Failed to retrieve generated id for employee.");
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            log.error("Error saving employee: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long departmentId = resultSet.getLong("department_id");
                Long employeeId = resultSet.getLong("id");

                Employee employee = new Employee();
                employee.setId(employeeId);
                employee.setName(resultSet.getString("name"));
                employee.setProjects(setProjects(employeeId));
                if (departmentId > 0)
                    employee.setDepartment(setDepartment(departmentId));
                employees.add(employee);
            }
        } catch (SQLException e) {
            log.error("Error finding all employees: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return employees;
    }

    private List<Project> setProjects(Long employeeId) {
        List<Project> projects = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECTS_BY_ID)) {
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Project project = new Project();
                project.setId(resultSet.getLong("id"));
                project.setName(resultSet.getString("name"));

                projects.add(project);
            }
        } catch (SQLException e) {
            log.error("Error retrieving projects for employee with id {}: {}", employeeId, e.getMessage());
            throw new RuntimeException(e);
        }
        return projects;
    }

    private Department setDepartment(Long departmentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DEPARTMENT)) {
            preparedStatement.setLong(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getLong("id"));
                department.setName(resultSet.getString("name"));
                return department;
            } else {
                log.warn("No department found with id {}", departmentId);
                return null;
            }
        } catch (SQLException e) {
            log.error("Error retrieving department with id {}: {}", departmentId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Employee> getById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setName(resultSet.getString("name"));

                Long departmentId = resultSet.getLong("department_id");
                employee.setDepartment(setDepartment(departmentId));

                employee.setProjects(setProjects(id));

                return Optional.of(employee);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error retrieving employee with id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Employee> update(Employee entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setLong(2, entity.getId());

            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            log.error("Error updating employee with ID {}: {}", entity.getId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)) {
            preparedStatement.setLong(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                log.warn("No employee found with id {} to delete", id);
            } else {
                log.info("Employee with id {} deleted successfully", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting employee with id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL)) {
            int rowsDeleted = preparedStatement.executeUpdate();
            log.info("{} rows deleted from employee table", rowsDeleted);
        } catch (SQLException e) {
            log.error("Error clearing employee table: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean addProjectToEmployee(Long employeeId, Long projectId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_PROJECT)) {

            preparedStatement.setLong(1, employeeId);
            preparedStatement.setLong(2, projectId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            log.error("Error adding project {} to employee {}: {}", projectId, employeeId, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean addDepartment(Long employeeId, Long departmentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     ADD_DEPARTMENT_TO_EMPLOYEE)) {
            statement.setLong(1, departmentId);
            statement.setLong(2, employeeId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            log.error("Error adding department to employee: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}