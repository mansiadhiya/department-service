package com.company.department.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeDto {

    private Long id;
    private String fullName;
    private String email;
    private Long departmentId;
    private BigDecimal salary;
    private LocalDate joiningDate;
}
