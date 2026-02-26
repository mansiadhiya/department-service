package com.company.department.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DepartmentTest {

    @Test
    void shouldCreateDepartmentWithBuilder() {
        Department dept = Department.builder()
                .id(1L)
                .name("Engineering")
                .location("Ahmedabad")
                .build();

        assertThat(dept.getId()).isEqualTo(1L);
        assertThat(dept.getName()).isEqualTo("Engineering");
        assertThat(dept.getLocation()).isEqualTo("Ahmedabad");
    }

    @Test
    void shouldSetAndGetProperties() {
        Department dept = new Department();
        dept.setId(2L);
        dept.setName("IT");
        dept.setLocation("Mumbai");

        assertThat(dept.getId()).isEqualTo(2L);
        assertThat(dept.getName()).isEqualTo("IT");
        assertThat(dept.getLocation()).isEqualTo("Mumbai");
    }
}
