package ru.soular.ewm.main.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.EventState;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventDAO extends CustomJpaRepository<Event, Long> {

    @Query("select e from Event as e where e.initiator.id in ?1 and e.state in ?2 and e.category.id in ?3 and e.eventDate > ?4 " +
            "and e.eventDate < ?5")
    List<Event> getEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')) " +
            "and e.category.id in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate >= ?4 " +
            "and e.eventDate <= ?4 ")
    List<Event> getEvents(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.description) like upper(concat('%', ?1, '%')) " +
            "and e.category.id in ?2 " +
            "and e.paid = ?3 " +
            "and e.eventDate >= ?4 " +
            "and e.eventDate <= ?4 " +
            "and e.confirmedRequests < e.participantLimit ")
    List<Event> getOnlyAvailableEvents(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Pageable pageable);

    List<Event> getEventsByInitiator(User initiator, Pageable pageable);

    List<Event> getEventsByCategory_Id(Long category_id);
}
