package ru.soular.ewm.main.participation.dao;

import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.participation.model.ParticipationRequest;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.RequestStatus;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.util.List;

public interface ParticipationRequestDAO extends CustomJpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> getAllByRequester(User requester);

    List<ParticipationRequest> getAllByEvent(Event event);

    List<ParticipationRequest> getAllByEventAndStatus(Event event, RequestStatus status);

    Boolean existsByRequesterAndEvent(User requester, Event event);

}
