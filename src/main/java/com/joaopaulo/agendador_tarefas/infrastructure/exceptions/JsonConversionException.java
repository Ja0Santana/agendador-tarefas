package com.joaopaulo.agendador_tarefas.infrastructure.exceptions;

public class JsonConversionException extends RuntimeException {
    public JsonConversionException(String message) {
        super(message);
    }
    public JsonConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
