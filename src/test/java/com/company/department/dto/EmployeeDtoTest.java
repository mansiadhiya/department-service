package com.company.department.dto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmployeeDtoTest {

    @Test
    void shouldCreateEmployeeDto() {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1L);
        dto.setFullName("John Doe");
        dto.setEmail("john@test.com");
        dto.setDepartmentId(10L);
        dto.setSalary(new java.math.BigDecimal("50000"));
        dto.setJoiningDate(java.time.LocalDate.now());

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getFullName()).isEqualTo("John Doe");
        assertThat(dto.getEmail()).isEqualTo("john@test.com");
        assertThat(dto.getDepartmentId()).isEqualTo(10L);
        assertThat(dto.getSalary()).isNotNull();
        assertThat(dto.getJoiningDate()).isNotNull();
    }
}
