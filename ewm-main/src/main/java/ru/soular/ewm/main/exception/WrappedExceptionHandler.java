package ru.soular.ewm.main.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.soular.ewm.main.exception.model.ApplicationException;
import ru.soular.ewm.main.exception.model.ExceptionResponse;

@Component
@RequiredArgsConstructor
public class WrappedExceptionHandler {
    private final ObjectMapper mapper;

    public <T> ResponseEntity<T> handleResponse(ResponseEntity<T> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            ExceptionResponse er;

            try {
                er = mapper.readValue((String) response.getBody(), ExceptionResponse.class);

            } catch (JsonProcessingException ex) {
                throw new ApplicationException("Error on handling external service exception " + ex.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            throw new ApplicationException(er);
        }

        return response;
    }
}
