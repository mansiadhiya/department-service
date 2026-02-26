package com.company.department.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.department.client.EmployeeClient;
import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentEmployeesResponse;
import com.company.department.dto.EmployeeDto;
import com.company.department.entity.Department;
import com.company.department.exception.ResourceNotFoundException;
import com.company.department.repository.DepartmentRepository;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository repository;

    @Mock
    private EmployeeClient employeeClient;

    @Mock
    private DepartmentMapper mapper;

    @InjectMocks
    private DepartmentServiceImpl service;

    private Department department;
    private DepartmentDTO dto;

    @BeforeEach
    void init() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");
        department.setLocation("Ahemdabad");

        dto = new DepartmentDTO();
        dto.setId(1L);
        dto.setName("Engineering");
        dto.setLocation("Ahemdabad");
    }

    @Nested
    @DisplayName("Create Department Tests")
    class CreateDepartment {

        @Test
        @DisplayName("Should create department successfully")
        void shouldCreateDepartment() {
            when(mapper.toEntity(dto)).thenReturn(department);
            when(repository.save(any(Department.class))).thenReturn(department);
            when(mapper.toDTO(department)).thenReturn(dto);

            DepartmentDTO result = service.createDepartment(dto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Engineering");
            verify(repository).save(any(Department.class));
        }
    }

    @Nested
    @DisplayName("Get All Departments Tests")
    class GetAllDepartments {

        @Test
        @DisplayName("Should return all departments")
        void shouldReturnAllDepartments() {
            Department dept2 = new Department();
            dept2.setId(2L);
            dept2.setName("IT");
            dept2.setLocation("Ahemdabad");

            DepartmentDTO dto2 = new DepartmentDTO();
            dto2.setId(2L);
            dto2.setName("IT");

            when(repository.findAll()).thenReturn(List.of(department, dept2));
            when(mapper.toDTO(department)).thenReturn(dto);
            when(mapper.toDTO(dept2)).thenReturn(dto2);

            List<DepartmentDTO> result = service.getAllDepartments();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("Should return empty list when no departments")
        void shouldReturnEmptyList() {
            when(repository.findAll()).thenReturn(List.of());

            List<DepartmentDTO> result = service.getAllDepartments();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Get Department Tests")
    class GetDepartment {

        @Test
        @DisplayName("Should return department when found")
        void shouldReturnDepartment() {
            when(repository.findById(1L)).thenReturn(Optional.of(department));
            when(mapper.toDTO(department)).thenReturn(dto);

            DepartmentDTO result = service.getDepartment(1L);

            assertThat(result).isNotNull();
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when department not found")
        void shouldThrowWhenNotFound() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getDepartment(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Department not found");
        }
    }

    @Nested
    @DisplayName("Get Employees Tests")
    class GetEmployees {

        @Test
        @DisplayName("Should return department with employees")
        void shouldReturnDepartmentWithEmployees() {
            EmployeeDto emp1 = new EmployeeDto();
            emp1.setId(1L);
            emp1.setFullName("John Doe");

            EmployeeDto emp2 = new EmployeeDto();
            emp2.setId(2L);
            emp2.setFullName("Jane Smith");

            when(repository.findById(1L)).thenReturn(Optional.of(department));
            when(employeeClient.getEmployeesByDepartment(1L)).thenReturn(List.of(emp1, emp2));

            DepartmentEmployeesResponse result = service.getEmployees(1L);

            assertThat(result).isNotNull();
            assertThat(result.getDepartmentId()).isEqualTo(1L);
            assertThat(result.getDepartmentName()).isEqualTo("Engineering");
            assertThat(result.getEmployees()).hasSize(2);
            verify(employeeClient).getEmployeesByDepartment(1L);
        }

        @Test
        @DisplayName("Should throw exception when department not found")
        void shouldThrowWhenDepartmentNotFound() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getEmployees(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Department not found");
            verify(employeeClient, never()).getEmployeesByDepartment(any());
        }

        @Test
        @DisplayName("Should return empty employees list")
        void shouldReturnEmptyEmployeesList() {
            when(repository.findById(1L)).thenReturn(Optional.of(department));
            when(employeeClient.getEmployeesByDepartment(1L)).thenReturn(List.of());

            DepartmentEmployeesResponse result = service.getEmployees(1L);

            assertThat(result.getEmployees()).isEmpty();
        }
    }
}
