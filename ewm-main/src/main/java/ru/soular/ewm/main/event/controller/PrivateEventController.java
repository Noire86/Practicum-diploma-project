package ru.soular.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.event.dto.NewEventDto;
import ru.soular.ewm.main.event.dto.UpdateEventRequest;
import ru.soular.ewm.main.event.service.PrivateEventService;
import ru.soular.ewm.main.participation.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {

    private final PrivateEventService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @PathVariable Long userId,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(service.getEvents(userId, from, size), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> update(@PathVariable Long userId, @RequestBody UpdateEventRequest updateEventRequest) {
        return new ResponseEntity<>(service.update(userId, updateEventRequest), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> create(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(service.create(userId, newEventDto), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getEvent(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return new ResponseEntity<>(service.cancelEvent(userId, eventId), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsByInitiator(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getRequestsByInitiator(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ResponseEntity<ParticipationRequestDto> confirmRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long requestId) {

        return new ResponseEntity<>(service.confirmRequest(userId, eventId, requestId), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<ParticipationRequestDto> rejectRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long requestId) {

        return new ResponseEntity<>(service.rejectRequest(userId, eventId, requestId), HttpStatus.OK);
    }

}
