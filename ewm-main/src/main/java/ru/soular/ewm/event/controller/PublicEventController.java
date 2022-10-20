package ru.soular.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.event.dto.EventFullDto;
import ru.soular.ewm.event.dto.EventShortDto;
import ru.soular.ewm.event.service.PublicEventService;
import ru.soular.ewm.util.EventSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {

    private final PublicEventService service;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam String text,
            @RequestParam List<Long> categories,
            @RequestParam Boolean paid,
            @RequestParam String rangeStart,
            @RequestParam String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam EventSort sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {

        return new ResponseEntity<>(service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request),
                HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> get(@PathVariable Long id, HttpServletRequest request) {
        return new ResponseEntity<>(service.get(id, request), HttpStatus.OK);
    }
}
