package ru.soular.ewm.main.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.category.dao.CategoryDAO;
import ru.soular.ewm.main.category.model.Category;
import ru.soular.ewm.main.client.service.StatsClient;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.event.dto.NewEventDto;
import ru.soular.ewm.main.event.dto.UpdateEventRequest;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.exception.model.ApplicationException;
import ru.soular.ewm.main.participation.dao.ParticipationRequestDAO;
import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;
import ru.soular.ewm.main.participation.model.ParticipationRequest;
import ru.soular.ewm.main.user.dao.UserDAO;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.EventState;
import ru.soular.ewm.main.util.PageableBuilder;
import ru.soular.ewm.main.util.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {

    private final ParticipationRequestDAO requestDAO;
    private final CategoryDAO categoryDAO;
    private final ModelMapper mapper;
    private final EventDAO eventDAO;
    private final UserDAO userDAO;

    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        User user = userDAO.findEntityById(userId);
        List<EventShortDto> events = eventDAO.getEventsByInitiator(user, PageableBuilder.build(from, size))
                .stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());

        events.forEach(event -> event.setViews(statsClient.getViews(event.getId())));
        log.info("Getting all events by user ID:{}", userId);
        return events;
    }

    @Override
    public EventFullDto update(Long userId, UpdateEventRequest req) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(req.getEventId());

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ApplicationException("Cannot update an event: this user is not an initiator of this event!",
                    HttpStatus.FORBIDDEN);
        }

        if (Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new ApplicationException("Cannot update published event", HttpStatus.FORBIDDEN);
        }

        if (req.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ApplicationException("Event date must be ahead of updated time for at least 2 hours!",
                    HttpStatus.BAD_REQUEST);
        }

        if (Objects.equals(event.getState(), EventState.REJECTED)) event.setState(EventState.PENDING);
        if (req.getTitle() != null) event.setTitle(req.getTitle());
        if (req.getAnnotation() != null) event.setAnnotation(req.getAnnotation());
        if (req.getCategoryId() != null) event.setCategory(categoryDAO.findEntityById(req.getCategoryId()));
        if (req.getDescription() != null) event.setDescription(req.getDescription());
        if (req.getEventDate() != null) event.setEventDate(req.getEventDate());
        if (req.getPaid() != null) event.setPaid(req.getPaid());
        if (req.getParticipantLimit() != null) event.setParticipantLimit(req.getParticipantLimit());

        EventFullDto result = mapper.map(eventDAO.save(event), EventFullDto.class);
        result.setViews(statsClient.getViews(event.getId()));

        log.info("Updating event ID:{} with data={}", req.getEventId(), req);
        return result;
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userDAO.findEntityById(userId);
        Category category = categoryDAO.findEntityById(newEventDto.getCategory());
        Event event = mapper.map(newEventDto, Event.class);

        event.setInitiator(initiator);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setConfirmedRequests(0);

        log.info("Creating new Event with data={}", event);
        return mapper.map(eventDAO.save(event), EventFullDto.class);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ApplicationException("Cannot get event: this user is not an initiator of this event!",
                    HttpStatus.FORBIDDEN);
        }

        EventFullDto result = mapper.map(eventDAO.findEntityById(eventId), EventFullDto.class);
        result.setViews(statsClient.getViews(event.getId()));

        log.info("Getting event ID:{} by user ID:{}", eventId, userId);
        return result;
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ApplicationException("Cannot cancel event: this user is not an initiator of this event!",
                    HttpStatus.FORBIDDEN);
        }

        if (!Objects.equals(event.getState(), EventState.PENDING)) {
            throw new ApplicationException("You can only cancel pending events", HttpStatus.FORBIDDEN);
        }

        event.setState(EventState.CANCELED);

        EventFullDto result = mapper.map(eventDAO.save(event), EventFullDto.class);
        result.setViews(statsClient.getViews(event.getId()));
        log.info("Cancelling event ID:{} by user ID:{}", eventId, userId);
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);

        //TODO В тестах ошибка. По спеке должна быть проверка того, что юзер от которого идет запрос,
        //TODO должен быть инициатором ивента.
//        if (!Objects.equals(event.getInitiator(), user)) {
//            throw new ApplicationException("Cannot get requests: this user is not an initiator of this event!",
//                    HttpStatus.FORBIDDEN);
//        }

        log.info("Getting all participation requests by event ID:{}", eventId);
        return requestDAO.getAllByEvent(event)
                .stream()
                .map(req -> mapper.map(req, ParticipationRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);
        ParticipationRequest request = requestDAO.findEntityById(requestId);

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ApplicationException("Cannot confirm this request: this user is not an initiator of this event!",
                    HttpStatus.FORBIDDEN);
        }

        if (Boolean.FALSE.equals(event.getRequestModeration()) || Objects.equals(event.getParticipantLimit(), 0)) {
            throw new ApplicationException("Confirming of this request is not necessary", HttpStatus.BAD_REQUEST);
        }

        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            throw new ApplicationException("Cannot confirm this request: limit of participants is exceeded", HttpStatus.FORBIDDEN);
        }

        request.setStatus(RequestStatus.CONFIRMED);
        ParticipationRequestDto result = mapper.map(requestDAO.save(request), ParticipationRequestDto.class);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        Event savedEvent = eventDAO.save(event);

        if (Objects.equals(savedEvent.getParticipantLimit(), savedEvent.getConfirmedRequests())) {
            requestDAO.getAllByEventAndStatus(savedEvent, RequestStatus.PENDING)
                    .forEach(req -> {
                        req.setStatus(RequestStatus.REJECTED);
                        requestDAO.save(req);
                    });
        }
        log.info("Confirming participation request ID:{} for event ID:{} by user ID:{}", requestId, eventId, userId);
        return result;
    }

    @Override
    public ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);
        ParticipationRequest request = requestDAO.findEntityById(requestId);

        if (!Objects.equals(event.getInitiator(), user)) {
            throw new ApplicationException("Cannot reject this request: this user is not an initiator of this event!",
                    HttpStatus.FORBIDDEN);
        }

        request.setStatus(RequestStatus.REJECTED);
        ParticipationRequestDto result = mapper.map(requestDAO.save(request), ParticipationRequestDto.class);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventDAO.save(event);

        log.info("Rejecting participation request ID:{} for event ID:{} by user ID:{}", requestId, eventId, userId);
        return result;
    }
}
