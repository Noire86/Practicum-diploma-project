package ru.soular.ewm.main.participation.dao;

import org.springframework.data.jpa.repository.Query;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.participation.model.ParticipationRequest;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.RequestStatus;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.util.List;

public interface ParticipationRequestDAO extends CustomJpaRepository<ParticipationRequest, Long> {

    /**
     * Получение запросов по автору
     */
    List<ParticipationRequest> getAllByRequester(User requester);

    /**
     * Получение всех запросов по событию
     */
    List<ParticipationRequest> getAllByEvent(Event event);

    /**
     * Получение всех запросов по статусу и событию
     */
    List<ParticipationRequest> getAllByEventAndStatus(Event event, RequestStatus status);

    /**
     * Быстрый подсчет подтвержденных запросов в событии
     */
    @Query("select count(r) from ParticipationRequest as r where r.event.id = ?1 and r.status = 'CONFIRMED'")
    Long countConfirmedRequests(Long eventId);

    /**
     * Проверка наличия запроса по автору и событию
     */
    Boolean existsByRequesterAndEvent(User requester, Event event);

}
