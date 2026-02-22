package com.joaopaulo.agendador_tarefas.infrastructure.exceptions.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private String error;
    private int status;
    private String message;
    private String path;
}
