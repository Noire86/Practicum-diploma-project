package ru.soular.ewm.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Исключение валидации сущностей
 */
public class ValidationException extends RuntimeException {

    private BindingResult errors;
    private String message;

    public ValidationException(String message) {
        this.message = message;
    }

    public ValidationException(BindingResult errors) {
        this.errors = errors;
    }

    public List<String> getMessages() {
        if (errors != null) {
            return errors.getAllErrors()
                    .stream()
                    .map(ValidationException::getValidationMessage)
                    .collect(Collectors.toList());
        } else {
            return List.of(message);
        }
    }

    public String getMessage() {
        if (message == null) {
            return String.join(" ", this.getMessages());
        }
        return message;
    }

    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError) {
            return error.getDefaultMessage();
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }
}