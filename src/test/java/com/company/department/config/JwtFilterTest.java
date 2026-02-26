package com.company.department.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    void shouldProcessValidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer validToken");

        Claims claims = new DefaultClaims();
        claims.setSubject("user@test.com");
        claims.put("role", "ADMIN");

        when(jwtUtil.validate("validToken")).thenReturn(claims);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtUtil).validate("validToken");
    }

    @Test
    void shouldProcessRequestWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtUtil, never()).validate(any());
    }

    @Test
    void shouldHandleInvalidToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.addHeader("Authorization", "Bearer invalidToken");

        when(jwtUtil.validate("invalidToken")).thenThrow(new RuntimeException("Invalid token"));

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtUtil).validate("invalidToken");
    }
}
