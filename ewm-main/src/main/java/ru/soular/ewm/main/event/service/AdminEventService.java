package ru.soular.ewm.main.event.service;

import ru.soular.ewm.main.event.dto.AdminUpdateEventRequest;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.util.EventState;

import java.util.List;

/**
 * Интерфейс админского сервиса событий
 */
public interface AdminEventService {
    List<EventFullDto> getEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds, String rangeStart,
                                 String rangeEnd, Integer from, Integer size);

    EventFullDto update(Long id, AdminUpdateEventRequest updateEventRequest);

    EventFullDto publish(Long id);

    EventFullDto reject(Long id);
}
