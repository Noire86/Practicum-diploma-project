package ru.soular.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.stats.dto.EndpointHitDto;
import ru.soular.ewm.stats.dto.ViewStatsDto;
import ru.soular.ewm.stats.service.StatsService;

import java.util.List;

/**
 * Контроллер эндпоинтов сборки статистики
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> create(@RequestBody EndpointHitDto endpointHitDto) {
        return new ResponseEntity<>(service.create(endpointHitDto), HttpStatus.OK);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> get(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        return new ResponseEntity<>(service.get(start, end, uris, unique), HttpStatus.OK);
    }
}
