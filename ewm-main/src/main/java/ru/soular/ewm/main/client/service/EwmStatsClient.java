package ru.soular.ewm.main.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.util.annotation.Nullable;
import ru.soular.ewm.main.client.dto.EndpointHitDto;

import java.util.List;

@Service
public class EwmStatsClient extends BaseClient {

    private final ObjectMapper mapper;

    @Autowired
    public EwmStatsClient(@Value("${ewm-stats.server.url}") String serverUrl, RestTemplateBuilder builder,
                          @Autowired ObjectMapper mapper) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
        this.mapper = mapper;
    }

    public void createEndpointHit(EndpointHitDto endpointHitDto) {
        post("/hit", endpointHitDto);
    }


    public Long getViews(Long eventId) {

    }


    public ResponseEntity<Object> getStats(List<String> uris, @Nullable String start, @Nullable String end, Boolean unique) {
        return get(String.format("/stats/?uris=%s&start=%s&end=%s&unique=%b",
                String.join(",", uris),
                start,
                end,
                unique));
    }

}
