package com.company.department.client;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeClientTest {

    @Mock
    private HttpServletRequest httpRequest;

    private EmployeeClient employeeClient;

    @BeforeEach
    void setUp() {
        employeeClient = new EmployeeClient(httpRequest);
    }

    @Test
    void shouldCreateEmployeeClient() {
        assertThat(employeeClient).isNotNull();
    }
}
