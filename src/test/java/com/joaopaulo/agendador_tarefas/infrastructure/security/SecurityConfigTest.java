package com.joaopaulo.agendador_tarefas.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired(required = false)
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("Deve carregar SecurityFilterChain corretamente")
    void deveCarregarBeansSeguranca() {
        assertThat(securityFilterChain).isNotNull();
    }
}
