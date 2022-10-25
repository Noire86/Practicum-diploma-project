package ru.soular.ewm.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.client.dto.EndpointHitDto;
import ru.soular.ewm.main.client.service.StatsClient;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.exception.model.ApplicationException;
import ru.soular.ewm.main.participation.dao.ParticipationRequestDAO;
import ru.soular.ewm.main.util.Constants;
import ru.soular.ewm.main.util.EventSort;
import ru.soular.ewm.main.util.EventState;
import ru.soular.ewm.main.util.PageableBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {

    private final EventDAO eventDAO;
    private final ParticipationRequestDAO requestDAO;
    private final ModelMapper mapper;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                         Integer size, HttpServletRequest request) {

        List<Event> events;
        LocalDateTime start = LocalDateTime.parse(rangeStart, Constants.FORMATTER);
        LocalDateTime end = LocalDateTime.parse(rangeStart, Constants.FORMATTER);

        if (onlyAvailable) {
            events = eventDAO.getOnlyAvailableEvents(text, categoryIds, paid, start, end, PageableBuilder.build(from, size));
        } else {
            events = eventDAO.getEvents(text, categoryIds, paid, start, end, PageableBuilder.build(from, size));
        }

        List<EventShortDto> result = events.stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());

        switch (sort) {
            case EVENT_DATE:
                result.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                result.sort(Comparator.comparing(EventShortDto::getViews));
                break;
        }
        result.forEach(dto -> dto.setViews(statsClient.getViews(dto.getId())));
        result.forEach(dto -> dto.setConfirmedRequests(requestDAO.countConfirmedRequests(dto.getId())));
        statsClient.createEndpointHit(EndpointHitDto.builder()
                .app("Explore With Me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());

        log.info("Getting all published events sorted by {}, availableOnly={}", sort.name(), onlyAvailable);
        return result;
    }

    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = eventDAO.findEntityById(id);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ApplicationException("Unable to get an unpublished event!", HttpStatus.FORBIDDEN);
        }

        EventFullDto result = mapper.map(event, EventFullDto.class);
        result.setViews(statsClient.getViews(event.getId()));
        result.setConfirmedRequests(requestDAO.countConfirmedRequests(result.getId()));
        statsClient.createEndpointHit(EndpointHitDto.builder()
                .app("Explore With Me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());

        log.info("Getting event by id={}", id);
        return result;
    }
}
