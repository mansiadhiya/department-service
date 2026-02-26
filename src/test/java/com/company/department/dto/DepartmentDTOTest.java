package com.company.department.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class DepartmentDTOTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldCreateDepartmentDTOWithBuilder() {
        DepartmentDTO dto = DepartmentDTO.builder()
                .id(1L)
                .name("Engineering")
                .location("Ahmedabad")
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Engineering");
        assertThat(dto.getLocation()).isEqualTo("Ahmedabad");
    }

    @Test
    void shouldFailValidationWhenNameIsBlank() {
        DepartmentDTO dto = DepartmentDTO.builder()
                .name("")
                .location("Ahmedabad")
                .build();

        Set<ConstraintViolation<DepartmentDTO>> violations = validator.validate(dto);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("required");
    }

    @Test
    void shouldPassValidationWithValidData() {
        DepartmentDTO dto = DepartmentDTO.builder()
                .name("Engineering")
                .location("Ahmedabad")
                .build();

        Set<ConstraintViolation<DepartmentDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
