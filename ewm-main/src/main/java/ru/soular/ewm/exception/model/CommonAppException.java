package ru.soular.ewm.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Абстрактное базовое исключение для приложения
 */
@Getter
public abstract class CommonAppException extends RuntimeException {
    private final String message;
    private final HttpStatus code;

    protected CommonAppException(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }
}