package com.company.department;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class DepartmentServiceApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldLoadApplicationContext() {
        assertThat(context).isNotNull();
    }

    @Test
    void mainMethodShouldRun() {
        assertThat(DepartmentServiceApplication.class).isNotNull();
        DepartmentServiceApplication.main(new String[]{});
    }
}
