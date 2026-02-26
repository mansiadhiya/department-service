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
	
    private final DepartmentRepository departmentRepository;
    private final EmployeeClient employeeClient;
    private final DepartmentMapper departmentMapper;
    
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDto) {
        log.info("Creating department with name={}", departmentDto.getName());
        Department departmentEntity = departmentMapper.toEntity(departmentDto);
        Department savedDepartment = departmentRepository.save(departmentEntity);
        log.info("Department created successfully with departmentId={}, name={}", savedDepartment.getId(), savedDepartment.getName());
        return departmentMapper.toDTO(savedDepartment);
    }
    
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        log.info("Fetching all departments");
        List<DepartmentDTO> departmentList = departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toDTO)
                .toList();
        log.info("Found {} departments", departmentList.size());
        return departmentList;
    }

	@Override
	public DepartmentDTO getDepartment(Long departmentId) {
		log.info("Fetching department with departmentId={}", departmentId);
		 Department foundDepartment = departmentRepository.findById(departmentId)
	                .orElseThrow(() -> {
	                    log.error("Department not found with departmentId={}", departmentId);
	                    return new ResourceNotFoundException("Department not found with id " + departmentId);
	                });
		log.info("Department found with departmentId={}, name={}", foundDepartment.getId(), foundDepartment.getName());
	        return departmentMapper.toDTO(foundDepartment);
	}

	@Override
	public DepartmentEmployeesResponse getEmployees(Long departmentId) {
		log.info("Fetching employees for departmentId={}", departmentId);
		  Department foundDepartment = departmentRepository.findById(departmentId)
	                .orElseThrow(() -> {
	                    log.error("Department not found with departmentId={}", departmentId);
	                    return new RuntimeException("Department not found");
	                });

	        List<EmployeeDto> departmentEmployees = employeeClient.getEmployeesByDepartment(departmentId);
	        log.info("Found {} employees for departmentId={}", departmentEmployees.size(), departmentId);

	        return new DepartmentEmployeesResponse(
	                foundDepartment.getId(),
	                foundDepartment.getName(),
	                departmentEmployees
	        );
	    }
	}



