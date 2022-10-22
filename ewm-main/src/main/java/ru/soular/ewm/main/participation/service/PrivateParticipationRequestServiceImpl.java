package ru.soular.ewm.main.participation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.exception.model.ApplicationException;
import ru.soular.ewm.main.participation.dao.ParticipationRequestDAO;
import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;
import ru.soular.ewm.main.participation.model.ParticipationRequest;
import ru.soular.ewm.main.user.dao.UserDAO;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.EventState;
import ru.soular.ewm.main.util.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivateParticipationRequestServiceImpl implements PrivateParticipationRequestService {

    private final ParticipationRequestDAO requestDAO;
    private final ModelMapper mapper;
    private final EventDAO eventDAO;
    private final UserDAO userDAO;

    @Override
    public List<ParticipationRequestDto> get(Long userId) {
        User user = userDAO.findEntityById(userId);

        log.info("Getting all requests for user ID:{}", userId);
        return requestDAO.getAllByRequester(user)
                .stream()
                .map(req -> mapper.map(req, ParticipationRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);

        if (requestDAO.existsByRequesterAndEvent(user, event)) {
            throw new ApplicationException(String.format("Request from user: %d for event: %d already exists!",
                    userId, eventId), HttpStatus.BAD_REQUEST);
        }

        if (Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new ApplicationException("You cannot create a request for your own event", HttpStatus.FORBIDDEN);
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ApplicationException("Cannot create a request for an unpublished event!", HttpStatus.FORBIDDEN);
        }

        if (event.getParticipantLimit() > 0 && Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
            throw new ApplicationException("Cannot create a request for this event: limit of participants have been reached!",
                    HttpStatus.FORBIDDEN);
        }

        ParticipationRequest req = new ParticipationRequest();
        req.setEvent(event);
        req.setRequester(user);
        req.setCreated(LocalDateTime.now());
        req.setStatus(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventDAO.save(event);

        log.info("Creating participation request by user ID:{} for event ID:{}", userId, eventId);
        return mapper.map(requestDAO.save(req), ParticipationRequestDto.class);

    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        User user = userDAO.findEntityById(userId);
        ParticipationRequest request = requestDAO.findEntityById(requestId);
        Event event = request.getEvent();

        if (!Objects.equals(request.getRequester(), user)) {
            throw new ApplicationException("Cannot cancel a request: this user is not an author of this request!",
                    HttpStatus.FORBIDDEN);
        }

        if (Objects.equals(request.getStatus(), RequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventDAO.save(event);
        }

        request.setStatus(RequestStatus.CANCELED);
        log.info("Canceling a request ID:{} by user ID:{}", requestId, userId);
        return mapper.map(requestDAO.save(request), ParticipationRequestDto.class);
    }
}
