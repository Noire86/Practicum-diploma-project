package ru.soular.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.stats.dao.EndpointHitDAO;
import ru.soular.ewm.stats.dto.EndpointHitDto;
import ru.soular.ewm.stats.dto.ViewStatsDto;
import ru.soular.ewm.stats.exception.model.ApplicationException;
import ru.soular.ewm.stats.model.EndpointHit;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static ru.soular.ewm.stats.util.Constants.FORMATTER;

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
        LocalDateTime startTime;
        LocalDateTime endTime;

        try {
            startTime = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new ApplicationException("Invalid starting date value!", HttpStatus.BAD_REQUEST);
        }

        try {
            endTime = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new ApplicationException("Invalid ending date value!", HttpStatus.BAD_REQUEST);
        }

        if (endTime.isBefore(startTime)) {
            throw new ApplicationException("Ending time is before starting time of the range", HttpStatus.BAD_REQUEST);
        }

        log.info("Getting stats for {} range from {} to {} (unique={})", uris, startTime, endTime, unique);
        for (String uri : uris) {
            List<EndpointHit> hits = hitDAO.getEndpointHitsBy(uri, startTime, endTime);

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
