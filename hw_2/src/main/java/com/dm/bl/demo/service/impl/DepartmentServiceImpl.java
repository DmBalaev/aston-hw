package com.dm.bl.demo.service.impl;


import com.dm.bl.demo.dto.DepartmentDto;
import com.dm.bl.demo.entity.Department;
import com.dm.bl.demo.exception.NotFoundEntity;
import com.dm.bl.demo.mapper.DepartmentConverter;
import com.dm.bl.demo.repository.Repository;
import com.dm.bl.demo.service.Service;

import java.util.List;

public class DepartmentServiceImpl implements Service<DepartmentDto, Long> {
    private final Repository<Department, Long> repositoryDepartment;
    private final DepartmentConverter converter;

    public DepartmentServiceImpl(Repository<Department, Long> repositoryDepartment, DepartmentConverter converter) {
        this.repositoryDepartment = repositoryDepartment;
        this.converter = converter;
    }


    @Override
    public DepartmentDto create(DepartmentDto request) {
        Department department = converter.dtoToEntity(request);
        Department savedDepartment  = repositoryDepartment.save(department).get();

        return converter.entityToDto(savedDepartment);
    }

    @Override
    public DepartmentDto read(Long id) {
        Department department = repositoryDepartment.getById(id)
                .orElseThrow(()-> new NotFoundEntity("Department not found."));

        return converter.entityToDto(department);
    }

    @Override
    public DepartmentDto update(DepartmentDto request) {
        Department department = converter.dtoToEntity(request);
        Department updatedDepartment = repositoryDepartment.update(department)
                .orElseThrow(()-> new NotFoundEntity("Department not found."));

        return converter.entityToDto(updatedDepartment);
    }

    @Override
    public void delete(Long id) {
        repositoryDepartment.delete(id);
    }

    @Override
    public List<DepartmentDto> readAll() {
        return converter.entitiesToDtos(repositoryDepartment.findAll());
    }
}