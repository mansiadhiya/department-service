package com.company.department.service;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.company.department.dto.DepartmentDTO;
import com.company.department.entity.Department;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
    
    Department toEntity(DepartmentDTO dto);
    
    DepartmentDTO toDTO(Department entity);
}
