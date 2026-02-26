package com.company.department.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Department not found");

        assertThat(exception.getMessage()).isEqualTo("Department not found");
    }

    @Test
    void shouldBeRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
