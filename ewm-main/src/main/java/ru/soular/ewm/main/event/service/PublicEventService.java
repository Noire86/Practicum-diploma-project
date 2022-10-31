package ru.soular.ewm.main.event.service;

import ru.soular.ewm.main.event.dto.EventFullDto;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.util.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс публичного пользовательского сервиса событий
 */
public interface PublicEventService {

    List<EventShortDto> getEvents(String text, List<Long> categoryIds, Boolean paid, String rangeStart, String rangeEnd,
                                  Boolean onlyAvailable, EventSort sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto get(Long id, HttpServletRequest request);
}
