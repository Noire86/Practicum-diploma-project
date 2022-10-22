package ru.soular.ewm.main.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    public <T> ResponseEntity<T> get(String path, ParameterizedTypeReference<T> typeReference) {
        return sendRequest(HttpMethod.GET, path, null, typeReference);
    }

    public <T> void post(String path, T body, ParameterizedTypeReference<T> typeReference) {
        sendRequest(HttpMethod.POST, path, body, typeReference);
    }

    private <T> ResponseEntity<T> sendRequest(HttpMethod method, String path, @Nullable T body, ParameterizedTypeReference<T> typeReference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<T> entity = new HttpEntity<>(body, headers);
        return rest.exchange(path, method, entity, typeReference);
    }
}
