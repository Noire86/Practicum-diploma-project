package ru.soular.ewm.main.compilation.service;

import ru.soular.ewm.main.compilation.dto.CompilationDto;

import java.util.List;
/**
 * Интерфейс публичного сервиса подборок
 */
public interface PublicCompilationService {

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto get(Long compId);
}
