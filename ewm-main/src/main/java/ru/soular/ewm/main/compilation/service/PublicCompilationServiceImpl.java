package ru.soular.ewm.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.compilation.dao.CompilationDAO;
import ru.soular.ewm.main.compilation.dto.CompilationDto;
import ru.soular.ewm.main.compilation.model.Compilation;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.util.PageableBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationDAO compilationDAO;
    private final ModelMapper mapper;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> comps = compilationDAO.getCompilationsByPinned(pinned, PageableBuilder.build(from, size));
        log.info(String.format("Getting all %s compilations", pinned ? "pinned" : "unpinned"));
        return comps.stream()
                .map(this::prepareCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto get(Long compId) {
        Compilation comp = compilationDAO.findEntityById(compId);
        log.info("Getting compilation ID:{}", compId);
        return prepareCompilationDto(comp);
    }


    private CompilationDto prepareCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents()
                .stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());

        //TODO проставить просмотры
        return mapper.map(compilation, CompilationDto.class).toBuilder()
                .events(events)
                .build();
    }
}
