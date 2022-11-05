package ru.soular.ewm.stats.service;

import ru.soular.ewm.stats.dto.EndpointHitDto;
import ru.soular.ewm.stats.dto.ViewStatsDto;

import java.util.List;

/**
 * Интерфейс сервиса статистики
 */
public interface StatsService {
    EndpointHitDto create(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> get(String start, String end, List<String> uris, Boolean unique);
}
