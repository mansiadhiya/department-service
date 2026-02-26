package com.company.department.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.company.department.entity.Department;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Test
    void shouldSaveDepartment() {
        Department dept = Department.builder()
                .name("Engineering")
                .location("Ahmedabad")
                .build();

        Department saved = repository.save(dept);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Engineering");
    }

    @Test
    void shouldFindDepartmentById() {
        Department dept = repository.save(Department.builder()
                .name("IT")
                .location("Mumbai")
                .build());

        Department found = repository.findById(dept.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("IT");
    }

    @Test
    void shouldReturnEmptyWhenDepartmentNotFound() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void shouldFindAllDepartments() {
        repository.save(Department.builder().name("Engineering").location("Ahmedabad").build());
        repository.save(Department.builder().name("IT").location("Mumbai").build());

        List<Department> departments = repository.findAll();

        assertThat(departments).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldDeleteDepartment() {
        Department dept = repository.save(Department.builder().name("HR").location("Delhi").build());
        Long id = dept.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }
}
