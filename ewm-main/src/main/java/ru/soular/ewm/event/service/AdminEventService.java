package ru.soular.ewm.event.service;

import ru.soular.ewm.event.dto.AdminUpdateEventRequest;
import ru.soular.ewm.event.dto.EventFullDto;
import ru.soular.ewm.util.EventState;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getEvents(List<Long> userIds, List<EventState> states, List<Long> categoryIds, String rangeStart,
                                 String rangeEnd, Integer from, Integer size);

    EventFullDto update(Long id, AdminUpdateEventRequest updateEventRequest);

    EventFullDto publish(Long id);

    EventFullDto reject(Long id);
}
