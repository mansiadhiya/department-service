package com.company.department.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentEmployeesResponse;
import com.company.department.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
	
    private final DepartmentService departmentService;
    
    @PostMapping
    public ResponseEntity<DepartmentDTO> create(@Valid @RequestBody DepartmentDTO departmentDto){
        return new ResponseEntity<>(departmentService.createDepartment(departmentDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAll(){
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> get(@PathVariable("id") Long departmentId){
        return ResponseEntity.ok(departmentService.getDepartment(departmentId));
    }
    
    @GetMapping("/{id}/employees")
    public DepartmentEmployeesResponse getDepartmentEmployees(
            @PathVariable("id") Long departmentId){
        return departmentService.getEmployees(departmentId);
    }
}
