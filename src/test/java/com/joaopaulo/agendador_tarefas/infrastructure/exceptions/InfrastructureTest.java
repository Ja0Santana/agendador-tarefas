package com.joaopaulo.agendador_tarefas.infrastructure.exceptions;

import com.joaopaulo.agendador_tarefas.infrastructure.exceptions.dtos.ErrorResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InfrastructureTest {

    @Test
    @DisplayName("Deve testar instancialização de todas as exceções customizadas")
    void deveTestarExcecoes() {
        assertThat(new JsonConversionException("Error", new RuntimeException())).isNotNull();
        assertThat(new JsonConversionException("Error", new RuntimeException("Cause"))).isNotNull();
        assertThat(new ResourceNotFoundException("Error")).isNotNull();
        assertThat(new UnauthorizedException("Error")).isNotNull();
    }

    @Test
    @DisplayName("Deve testar ErrorResponseDTO")
    void deveTestarErrorResponseDTO() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .timestamp(now)
                .status(400)
                .error("Bad Request")
                .message("Message")
                .path("/path")
                .build();

        assertThat(dto.getTimestamp()).isEqualTo(now);
        assertThat(dto.getStatus()).isEqualTo(400);
        assertThat(dto.getError()).isEqualTo("Bad Request");
        assertThat(dto.getMessage()).isEqualTo("Message");
        assertThat(dto.getPath()).isEqualTo("/path");

        ErrorResponseDTO emptyDto = new ErrorResponseDTO();
        emptyDto.setStatus(200);
        assertThat(emptyDto.getStatus()).isEqualTo(200);
    }
}
