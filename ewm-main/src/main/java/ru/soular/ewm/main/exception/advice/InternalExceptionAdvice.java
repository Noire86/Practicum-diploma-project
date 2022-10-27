package ru.soular.ewm.main.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.soular.ewm.main.exception.model.ExceptionResponse;
import ru.soular.ewm.main.util.Constants;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Order
@RestControllerAdvice
public class InternalExceptionAdvice {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(Throwable ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .reason("Internal server error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(Constants.FORMATTER))
                .errors(Collections.emptyList())
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
