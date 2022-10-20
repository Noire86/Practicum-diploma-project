package ru.soular.ewm.participation.dao;

import ru.soular.ewm.event.model.Event;
import ru.soular.ewm.participation.model.ParticipationRequest;
import ru.soular.ewm.user.model.User;
import ru.soular.ewm.util.RequestStatus;
import ru.soular.ewm.util.jpa.CustomJpaRepository;

import java.util.List;

public interface ParticipationRequestDAO extends CustomJpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> getAllByRequester(User requester);

    List<ParticipationRequest> getAllByEvent(Event event);

    List<ParticipationRequest> getAllByEventAndStatus(Event event, RequestStatus status);

    Boolean existsByRequesterAndEvent(User requester, Event event);

}
