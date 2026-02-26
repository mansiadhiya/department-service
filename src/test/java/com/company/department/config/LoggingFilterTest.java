package com.company.department.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class LoggingFilterTest {

    private final LoggingFilter filter = new LoggingFilter();

    @Test
    void shouldAddTraceHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader("X-Trace-Id")).isNotNull();
        assertThat(response.getHeader("X-Correlation-Id")).isNotNull();
        assertThat(response.getHeader("X-Request-Id")).isNotNull();
    }

    @Test
    void shouldUseExistingTraceId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.addHeader("X-Trace-Id", "existing-trace-id");

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader("X-Trace-Id")).isEqualTo("existing-trace-id");
    }
}
