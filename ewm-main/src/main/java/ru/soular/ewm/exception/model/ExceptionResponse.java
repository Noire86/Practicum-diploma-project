package ru.soular.ewm.exception.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Модель для эдвайса исключений
 */
@Data
@Builder
public class ExceptionResponse {
    private List<String> errors;
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}