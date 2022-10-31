package ru.soular.ewm.main.participation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;
import ru.soular.ewm.main.participation.service.PrivateParticipationRequestService;

import java.util.List;

/**
 * Контроллер приватных эндпоинтов запросов на участие
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateParticipationRequestController {

    private final PrivateParticipationRequestService service;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> get(@PathVariable Long userId) {
        return new ResponseEntity<>(service.get(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> create(@PathVariable Long userId, @RequestParam Long eventId) {
        return new ResponseEntity<>(service.create(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancel(@PathVariable Long userId, @PathVariable Long requestId) {
        return new ResponseEntity<>(service.cancel(userId, requestId), HttpStatus.OK);
    }
}
