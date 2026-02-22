package com.company.department.service;

import java.util.List;

import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentEmployeesResponse;

public interface DepartmentService {

	DepartmentDTO createDepartment(DepartmentDTO dto);

	List<DepartmentDTO> getAllDepartments();
	
    DepartmentDTO getDepartment(Long id);

	DepartmentEmployeesResponse getEmployees(Long id);

}
