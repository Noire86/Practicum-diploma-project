package ru.soular.ewm.main.compilation.service;

import ru.soular.ewm.main.compilation.dto.CompilationDto;
import ru.soular.ewm.main.compilation.dto.NewCompilationDto;

/**
 * Интерфейс админского сервиса подборок
 */
public interface AdminCompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    void deleteEvent(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);

    void unpin(Long compId);

    void pin(Long compId);
}
