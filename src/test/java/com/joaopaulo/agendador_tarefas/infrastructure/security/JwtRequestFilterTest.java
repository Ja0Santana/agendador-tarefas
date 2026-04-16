package com.joaopaulo.agendador_tarefas.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve autenticar usuário quando token for válido")
    void deveAutenticarUsuárioComTokenValido() throws Exception {
        String token = "valid-token";
        String username = "user@test.com";
        String authHeader = "Bearer " + token;
        UserDetails userDetails = new User(username, "pass", Collections.emptyList());

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(username);
        when(userDetailsService.carregarDadosUsuario(username, authHeader)).thenReturn(userDetails);
        when(jwtUtil.validateToken(token, username)).thenReturn(true);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo(username);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve ignorar requisição sem cabeçalho Authorization")
    void deveIgnorarSemHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve retornar 401 quando token expirar")
    void deveRetornar401TokenExpirado() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer expired");
        when(jwtUtil.extractUsername(anyString())).thenThrow(new ExpiredJwtException(null, null, "Expired"));
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        when(request.getRequestURI()).thenReturn("/test");

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(stringWriter.toString()).contains("Token expirado");
    }
}
