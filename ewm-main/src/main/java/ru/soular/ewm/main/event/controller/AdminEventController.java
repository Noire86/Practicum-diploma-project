package ru.soular.ewm.main.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.event.dto.AdminUpdateEventRequest;
import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.service.AdminEventService;
import ru.soular.ewm.main.util.EventState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final AdminEventService service;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam List<Long> users,
            @RequestParam List<EventState> states,
            @RequestParam List<Long> categories,
            @RequestParam String rangeStart,
            @RequestParam String rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventFullDto> editEvent(
            @PathVariable Long id,
            @RequestBody AdminUpdateEventRequest updateEventRequest) {

        return new ResponseEntity<>(service.update(id, updateEventRequest), HttpStatus.OK);
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable Long id) {
        return new ResponseEntity<>(service.publish(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable Long id) {
        return new ResponseEntity<>(service.reject(id), HttpStatus.OK);
    }
}
