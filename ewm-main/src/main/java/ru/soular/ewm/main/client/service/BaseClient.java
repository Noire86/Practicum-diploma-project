package ru.soular.ewm.main.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * RestTemplate клиент для межсервисного общения
 */
@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    public <T> ResponseEntity<Object> get(String path) {
        return sendRequest(HttpMethod.GET, path, null);
    }

    public <T> void post(String path, T body) {
        sendRequest(HttpMethod.POST, path, body);
    }

    /**
     * Метод для сборки и отправки запроса на другие сервисы
     */
    private <T> ResponseEntity<Object> sendRequest(HttpMethod method, String path, @Nullable T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<T> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Object> response;
        try {
            response = rest.exchange(path, method, entity, Object.class);

        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
        return response;
    }
}
