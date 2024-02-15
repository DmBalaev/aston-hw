package com.dm.bl.demo.service.impl;


import com.dm.bl.demo.dto.ApiResponse;
import com.dm.bl.demo.entity.Employee;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.repository.JDBCRepositoryEmployee;
import com.dm.bl.demo.service.Service;

import java.util.List;

public class EmployeeServiceImpl implements Service<Employee, Long> {
    private final JDBCRepositoryEmployee employeeRepository;

    public EmployeeServiceImpl(JDBCRepositoryEmployee employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee employee) {
        return employeeRepository.save(employee)
                .orElseThrow(()-> new RuntimeException("Не получилось"));
    }

    @Override
    public Employee read(Long id) {
        return employeeRepository.getById(id)
                .orElseThrow(() -> new NotFoundEntity("Employee not found"));
    }

    @Override
    public Employee update(Employee employee) {
        return employeeRepository.update(employee)
                .orElseThrow(() -> new NotFoundEntity("Employee not found"));
    }

    @Override
    public void delete(Long id) {
        employeeRepository.delete(id);
    }

    @Override
    public List<Employee> readAll() {
        return employeeRepository.findAll();
    }

    public ApiResponse addProject(Long employeeId, Long projectId) {
        boolean success = employeeRepository.addProjectToEmployee(employeeId, projectId);
        return success ?
                new ApiResponse("200", "Successful") :
                new ApiResponse("404", "Not Found");
    }

    public ApiResponse addDepartment(Long employeeId, Long departmentId) {
        boolean success = employeeRepository.addDepartment(employeeId, departmentId);
        return success ?
                new ApiResponse("200", "Successful") :
                new ApiResponse("404", "Not Found");
    }
}