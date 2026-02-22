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

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService{
	
    private final DepartmentRepository repository;
    private final EmployeeClient employeeClient;
    
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO dto) {
        Department entity = DepartmentMapper.toEntity(dto);
        Department saved = repository.save(entity);
        return DepartmentMapper.toDTO(saved);
    }
    
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return repository.findAll()
                .stream()
                .map(DepartmentMapper::toDTO)
                .toList();
    }

	@Override
	public DepartmentDTO getDepartment(Long id) {
		 Department dep = repository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));

	        return DepartmentMapper.toDTO(dep);
	}

	@Override
	public DepartmentEmployeesResponse getEmployees(Long id) {
		  Department dept = repository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Department not found"));

	        List<EmployeeDto> employees =
	                employeeClient.getEmployeesByDepartment(id);

	        return new DepartmentEmployeesResponse(
	                dept.getId(),
	                dept.getName(),
	                employees
	        );
	    }
	}



