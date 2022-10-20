package ru.soular.ewm.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    @Getter
    private final HttpStatus code;

    public ApplicationException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
