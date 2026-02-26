package com.company.department.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.company.department.dto.DepartmentDTO;
import com.company.department.entity.Department;

class DepartmentMapperTest {

    private final DepartmentMapper mapper = Mappers.getMapper(DepartmentMapper.class);

    @Test
    void shouldMapDtoToEntity() {
        DepartmentDTO dto = DepartmentDTO.builder()
                .id(1L)
                .name("Engineering")
                .location("Ahmedabad")
                .build();

        Department entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Engineering");
        assertThat(entity.getLocation()).isEqualTo("Ahmedabad");
    }

    @Test
    void shouldMapEntityToDto() {
        Department entity = Department.builder()
                .id(1L)
                .name("IT")
                .location("Mumbai")
                .build();

        DepartmentDTO dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("IT");
        assertThat(dto.getLocation()).isEqualTo("Mumbai");
    }

    @Test
    void shouldHandleNullDto() {
        Department entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void shouldHandleNullEntity() {
        DepartmentDTO dto = mapper.toDTO(null);
        assertThat(dto).isNull();
    }
}
