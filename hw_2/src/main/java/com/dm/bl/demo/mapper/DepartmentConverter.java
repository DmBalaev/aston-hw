package com.dm.bl.demo.mapper;



import com.dm.bl.demo.dto.DepartmentDto;
import com.dm.bl.demo.entity.Department;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentConverter implements Converter<Department, DepartmentDto> {
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public DepartmentDto entityToDto(Department department) {
        return mapper.map(department, DepartmentDto.class);
    }

    @Override
    public Department dtoToEntity(DepartmentDto departmentDto) {
        return mapper.map(departmentDto, Department.class);
    }

    @Override
    public List<DepartmentDto> entitiesToDtos(List<Department> departments) {
        return departments.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}