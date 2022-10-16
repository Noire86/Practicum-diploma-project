package ru.soular.ewm.exception.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.soular.ewm.exception.model.CommonAppException;
import ru.soular.ewm.exception.model.ExceptionResponse;
import ru.soular.ewm.exception.model.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @ExceptionHandler({CommonAppException.class, ValidationException.class})
    public ResponseEntity<Object> handleCommonException(CommonAppException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(ex.getCode().getReasonPhrase())
                .reason("")
                .message(ex.getMessage())
                .timestamp(String.format("[%s]", LocalDateTime.now().format(formatter)))
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, ex.getCode());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ExceptionResponse response = ExceptionResponse.builder()
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .reason("Integrity constraint has been violated")
                .message(ex.getSQLException().getMessage())
                .timestamp(String.format("[%s]", LocalDateTime.now().format(formatter)))
                .build();

        log.warn(String.format("%s is thrown: %s", ex.getClass().getSimpleName(), ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}
