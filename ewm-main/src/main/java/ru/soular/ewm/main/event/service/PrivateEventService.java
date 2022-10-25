package ru.soular.ewm.main.event.service;

import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.event.dto.NewEventDto;
import ru.soular.ewm.main.event.dto.UpdateEventRequest;
import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventFullDto update(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto create(Long userId, NewEventDto newEventDto);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByInitiator(Long userId, Long eventId);

    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long requestId);
}
