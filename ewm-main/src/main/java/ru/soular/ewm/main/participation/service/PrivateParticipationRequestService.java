package ru.soular.ewm.main.participation.service;

import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Интерфейс приватного сервиса запросов на участие
 */
public interface PrivateParticipationRequestService {
    List<ParticipationRequestDto> get(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
