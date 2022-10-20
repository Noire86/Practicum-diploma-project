package ru.soular.ewm.participation.service;

import ru.soular.ewm.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateParticipationRequestService {
    List<ParticipationRequestDto> get(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
