package ru.soular.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.soular.ewm.stats.dao.EndpointHitDAO;
import ru.soular.ewm.stats.dto.EndpointHitDto;
import ru.soular.ewm.stats.dto.ViewStatsDto;
import ru.soular.ewm.stats.model.EndpointHit;
import ru.soular.ewm.stats.util.Constants;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.soular.ewm.stats.util.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final ModelMapper mapper;
    private final EndpointHitDAO hitDAO;

    @Override
    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        EndpointHit hit = mapper.map(endpointHitDto, EndpointHit.class);
        log.info("Creating new EndpointHit data={}", endpointHitDto);

        return mapper.map(hitDAO.save(hit), EndpointHitDto.class);
    }

    @Override
    public List<ViewStatsDto> get(String start, String end, List<String> uris, Boolean unique) {
        List<ViewStatsDto> result = new ArrayList<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;

        if (Objects.nonNull(start)) {
            startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER);
        }

        if (Objects.nonNull(end)) {
            endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER);
        }

        log.info("Getting stats for {} range from {} to {} (unique={})", uris, startTime, endTime, unique);
        for (String uri : uris) {
            List<EndpointHit> hits =
                    (Objects.nonNull(startTime) && Objects.nonNull(endTime))
                            ? hitDAO.getEndpointHitsBy(uri, startTime, endTime)
                            : hitDAO.getAllByUri(uri);

            if (!hits.isEmpty()) {
                ViewStatsDto stats = ViewStatsDto.builder()
                        .uri(uri)
                        .app(hits.stream().map(EndpointHit::getApp).distinct().findAny().orElse(null))
                        .hits(unique ? hits.stream().map(EndpointHit::getIp).distinct().count() : hits.size())
                        .build();

                result.add(stats);
            }
        }

        return result;
    }
}
