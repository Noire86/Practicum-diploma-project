package ru.soular.ewm.main.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.soular.ewm.main.client.dto.EndpointHitDto;
import ru.soular.ewm.main.client.dto.ViewStatsDto;
import ru.soular.ewm.main.exception.WrappedExceptionHandler;

import java.util.List;
import java.util.Objects;

@Service
public class StatsClient extends BaseClient {

    private final WrappedExceptionHandler wrappedExceptionHandler;

    @Value("${stats.client.start-time}")
    private String START_TIME;

    @Value("${stats.client.end-time}")
    private String END_TIME;

    @Autowired
    public StatsClient(@Value("${ewm-stats.server.url}") String serverUrl, RestTemplateBuilder builder,
                       @Autowired WrappedExceptionHandler handler) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.wrappedExceptionHandler = handler;
    }

    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }

    public Long getViews(Long eventId) {
        ResponseEntity<Object> response = getStats(
                List.of("/events/" + eventId),
                START_TIME,
                END_TIME,
                false);

        List<ViewStatsDto> viewStats = (List<ViewStatsDto>) wrappedExceptionHandler.handleResponse(response).getBody();
        if (Objects.nonNull(viewStats) && !viewStats.isEmpty()) {
            return viewStats.stream()
                    .mapToLong(ViewStatsDto::getHits)
                    .sum();
        }

        return 0L;
    }

    public ResponseEntity<Object> getStats(List<String> uris, String start, String end, Boolean unique) {
        return get(String.format("/stats/?uris=%s&start=%s&end=%s&unique=%b",
                String.join(",", uris),
                start,
                end,
                unique));
    }
}
