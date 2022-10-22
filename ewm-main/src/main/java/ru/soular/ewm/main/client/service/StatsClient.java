package ru.soular.ewm.main.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.util.annotation.Nullable;
import ru.soular.ewm.main.client.dto.EndpointHitDto;
import ru.soular.ewm.main.client.dto.ViewStatsDto;

import java.util.List;
import java.util.Objects;

@Service
public class StatsClient extends BaseClient {

    @Autowired
    public StatsClient(@Value("${ewm-stats.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto, new ParameterizedTypeReference<>() {});
    }

    public Long getViews(Long eventId) {
        List<ViewStatsDto> viewStats = getStats(List.of("/events/" + eventId), null, null, false)
                .getBody();

        if (Objects.nonNull(viewStats) && !viewStats.isEmpty()) {
            return viewStats.stream()
                    .mapToLong(ViewStatsDto::getHits)
                    .sum();
        }

        return 0L;
    }


    public ResponseEntity<List<ViewStatsDto>> getStats(List<String> uris, @Nullable String start, @Nullable String end, Boolean unique) {
        return get(String.format("/stats/?uris=%s&start=%s&end=%s&unique=%b",
                        String.join(",", uris),
                        start,
                        end,
                        unique),
                new ParameterizedTypeReference<>() {
                });
    }

}
