package com.company.department.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.company.department.client.EmployeeClient;
import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentEmployeesResponse;
import com.company.department.dto.EmployeeDto;
import com.company.department.entity.Department;
import com.company.department.exception.ResourceNotFoundException;
import com.company.department.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DepartmentServiceImpl implements DepartmentService{
	
    private final DepartmentRepository repository;
    private final EmployeeClient employeeClient;
    
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        log.info("Creating department with name={}", dto.getName());
        Department entity = DepartmentMapper.toEntity(dto);
        Department saved = repository.save(entity);
        log.info("Department created successfully with departmentId={}, name={}", saved.getId(), saved.getName());
        return DepartmentMapper.toDTO(saved);
    }
    
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentDTO> departments = repository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .toList();
        log.info("Found {} departments", departments.size());
        return departments;
    }

	@Override
	public DepartmentDTO getDepartment(Long id) {
		log.info("Fetching department with departmentId={}", id);
		 Department dep = repository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Department not found with departmentId={}", id);
	                    return new ResourceNotFoundException("Department not found with id " + id);
	                });
		log.info("Department found with departmentId={}, name={}", dep.getId(), dep.getName());
	        return DepartmentMapper.toDTO(dep);
	}

	@Override
	public DepartmentEmployeesResponse getEmployees(Long id) {
		log.info("Fetching employees for departmentId={}", id);
		  Department dept = repository.findById(id)
	                .orElseThrow(() -> {
	                    log.error("Department not found with departmentId={}", id);
	                    return new RuntimeException("Department not found");
	                });

	        List<EmployeeDto> employees = employeeClient.getEmployeesByDepartment(id);
	        log.info("Found {} employees for departmentId={}", employees.size(), id);

	        return new DepartmentEmployeesResponse(
	                dept.getId(),
	                dept.getName(),
	                employees
	        );
	    }
	}



