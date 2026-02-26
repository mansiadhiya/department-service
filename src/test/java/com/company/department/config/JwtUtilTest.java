package com.company.department.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String secret = "mySecretKeyForTestingPurposesOnly1234567890";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
    }

    @Test
    void shouldValidateToken() {
        String token = Jwts.builder()
                .setSubject("user@test.com")
                .claim("role", "ADMIN")
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        Claims claims = jwtUtil.validate(token);

        assertThat(claims.getSubject()).isEqualTo("user@test.com");
        assertThat(claims).containsEntry("role", "ADMIN");
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.validate("invalid.token.here"))
                .isInstanceOf(Exception.class);
    }
}
