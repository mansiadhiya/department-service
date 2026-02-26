package com.company.department.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class DepartmentEmployeesResponseTest {

    @Test
    void shouldCreateDepartmentEmployeesResponse() {
        EmployeeDto emp = new EmployeeDto();
        emp.setId(1L);
        emp.setFullName("John Doe");

        DepartmentEmployeesResponse response = new DepartmentEmployeesResponse();
        response.setDepartmentId(1L);
        response.setDepartmentName("Engineering");
        response.setEmployees(List.of(emp));

        assertThat(response.getDepartmentId()).isEqualTo(1L);
        assertThat(response.getDepartmentName()).isEqualTo("Engineering");
        assertThat(response.getEmployees()).hasSize(1);
    }

    @Test
    void shouldCreateWithConstructor() {
        EmployeeDto emp = new EmployeeDto();
        emp.setId(1L);

        DepartmentEmployeesResponse response = new DepartmentEmployeesResponse(1L, "IT", List.of(emp));

        assertThat(response.getDepartmentId()).isEqualTo(1L);
        assertThat(response.getDepartmentName()).isEqualTo("IT");
        assertThat(response.getEmployees()).hasSize(1);
    }
}
