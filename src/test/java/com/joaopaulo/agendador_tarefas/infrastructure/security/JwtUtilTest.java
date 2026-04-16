package com.joaopaulo.agendador_tarefas.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secretKey = "YTM0NTY3ODlhY2RlZmdpamprbG1ub3BxcnN0dXZ3eHl6MTIzNDU2Nzg5MGFiY2RlZmdpaWpqa2xtbm9wcXJzdHV2d3h5ejEyMzQ1Njc4OTA=";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secretKey);
    }

    @Test
    @DisplayName("Deve extrair username corretamente de um token válido")
    void deveExtrairUsername() {
        String token = generateToken("user@test.com", 3600000);
        String username = jwtUtil.extractUsername(token);
        assertThat(username).isEqualTo("user@test.com");
    }

    @Test
    @DisplayName("Deve validar token com sucesso")
    void deveValidarToken() {
        String token = generateToken("user@test.com", 3600000);
        boolean isValid = jwtUtil.validateToken(token, "user@test.com");
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Deve invalidar token para usuário diferente")
    void deveInvalidarTokenUsuarioDiferente() {
        String token = generateToken("user@test.com", 3600000);
        boolean isValid = jwtUtil.validateToken(token, "other@test.com");
        assertThat(isValid).isFalse();
    }

    private String generateToken(String subject, long expirationMillis) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }
}
