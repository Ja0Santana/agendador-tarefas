package com.joaopaulo.agendador_tarefas.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("null")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        // Gera uma chave segura dinâmica para o teste (HS256 necessita de 256 bits)
        javax.crypto.SecretKey key = io.jsonwebtoken.Jwts.SIG.HS256.key().build();
        String dynamicSecret = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        
        ReflectionTestUtils.setField(jwtUtil, "secretKey", dynamicSecret);
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
        String secretKey = (String) org.springframework.test.util.ReflectionTestUtils.getField(jwtUtil, "secretKey");
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        javax.crypto.SecretKey key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);

        return io.jsonwebtoken.Jwts.builder()
                .subject(subject)
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key)
                .compact();
    }
}


