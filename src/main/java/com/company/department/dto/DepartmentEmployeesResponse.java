package com.company.department.dto;

import java.util.List;
import lombok.*;

@Getter
@AllArgsConstructor
public class DepartmentEmployeesResponse {

    private Long departmentId;
    private String departmentName;
    private List<EmployeeDto> employees;
}
