package ru.soular.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.category.dao.CategoryDAO;
import ru.soular.ewm.event.dao.EventDAO;
import ru.soular.ewm.event.dto.AdminUpdateEventRequest;
import ru.soular.ewm.event.dto.EventFullDto;
import ru.soular.ewm.event.model.Event;
import ru.soular.ewm.exception.model.ApplicationException;
import ru.soular.ewm.util.Constants;
import ru.soular.ewm.util.EventState;
import ru.soular.ewm.util.PageableBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventDAO eventDAO;
    private final CategoryDAO categoryDAO;
    private final ModelMapper mapper;

    @Override
    public List<EventFullDto> getEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds,
                                        String rangeStart, String rangeEnd, Integer from, Integer size) {
        LocalDateTime start = LocalDateTime.parse(rangeStart, Constants.FORMATTER);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, Constants.FORMATTER);

        List<EventFullDto> result = eventDAO.getEvents(userIds, states, categoryIds, start, end,
                        PageableBuilder.build(from, size)).stream()
                .map(event -> mapper.map(event, EventFullDto.class))
                .collect(Collectors.toList());


        //TODO добавить проставление просмотров события
        log.info("Getting all events as an administrator");
        return result;
    }

    @Override
    public EventFullDto update(Long id, AdminUpdateEventRequest dto) {
        Event event = eventDAO.findEntityById(id);

        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategoryId() != null) event.setCategory(categoryDAO.findEntityById(dto.getCategoryId()));
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getLocation() != null) {
            event.setLocationLat(dto.getLocation().getLat());
            event.setLocationLon(dto.getLocation().getLon());
        }

        EventFullDto result = mapper.map(eventDAO.save(event), EventFullDto.class);
        //TODO добавить проставку просмотров
        log.info("Updating Event ID: {} with new data={}", id, dto);
        return result;
    }

    @Override
    public EventFullDto publish(Long id) {
        Event event = eventDAO.findEntityById(id);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ApplicationException("Event date must be ahead of the publishing time for at least 1 hour!",
                    HttpStatus.BAD_REQUEST);
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ApplicationException("Only pending events can be published",
                    HttpStatus.BAD_REQUEST);
        }

        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);

        EventFullDto result = mapper.map(eventDAO.save(event), EventFullDto.class);
        //TODO добавить проставку просмотров
        log.info("Publishing new Event ID:{} by an Administrator", id);
        return result;
    }

    @Override
    public EventFullDto reject(Long id) {
        Event event = eventDAO.findEntityById(id);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ApplicationException("Only pending event can be rejected",
                    HttpStatus.BAD_REQUEST);
        }

        event.setState(EventState.REJECTED);
        EventFullDto result = mapper.map(eventDAO.save(event), EventFullDto.class);
        //TODO добавить проставку просмотров
        log.info("Publishing new Event ID:{} by an Administrator", id);
        return result;
    }
}
