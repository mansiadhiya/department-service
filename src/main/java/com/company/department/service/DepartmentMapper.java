package com.company.department.service;

import com.company.department.dto.DepartmentDTO;
import com.company.department.entity.Department;

public class DepartmentMapper {
	
    public static Department toEntity(DepartmentDTO dto){
        return Department.builder()
                .id(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .build();
    }

    public static DepartmentDTO toDTO(Department entity){
        return DepartmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .build();
    }


}
