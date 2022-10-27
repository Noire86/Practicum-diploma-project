package ru.soular.ewm.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.client.service.StatsClient;
import ru.soular.ewm.main.compilation.dao.CompilationDAO;
import ru.soular.ewm.main.compilation.dto.CompilationDto;
import ru.soular.ewm.main.compilation.dto.NewCompilationDto;
import ru.soular.ewm.main.compilation.model.Compilation;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.event.dto.EventShortDto;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.util.mapper.CustomModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationDAO compilationDAO;
    private final CustomModelMapper mapper;
    private final EventDAO eventDAO;
    private final StatsClient statsClient;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapper.map(newCompilationDto, Compilation.class);
        compilation.setEvents(eventDAO.findAllById(newCompilationDto.getEvents()));

        List<EventShortDto> events = compilation.getEvents()
                .stream()
                .map(event -> mapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());
        events.forEach(event -> event.setViews(statsClient.getViews(event.getId())));

        log.info("Creating new compilation with data={}", newCompilationDto);
        return mapper.map(compilationDAO.save(compilation), CompilationDto.class)
                .toBuilder()
                .events(events)
                .build();

    }

    @Override
    public void delete(Long compId) {
        log.info("Deleting compilation ID:{}", compId);
        if (compilationDAO.existsById(compId)) compilationDAO.deleteById(compId);
    }

    @Override
    public void deleteEvent(Long compId, Long eventId) {
        Compilation comp = compilationDAO.findEntityById(compId);
        Event event = eventDAO.findEntityById(eventId);

        log.info("Removing Event ID:{} from Compilation ID:{}", eventId, compId);
        comp.getEvents().remove(event);
        compilationDAO.save(comp);
    }

    @Override
    public void addEvent(Long compId, Long eventId) {
        Compilation comp = compilationDAO.findEntityById(compId);
        Event event = eventDAO.findEntityById(eventId);

        log.info("Adding Event ID:{} from Compilation ID:{}", eventId, compId);
        comp.getEvents().add(event);
        compilationDAO.save(comp);
    }

    @Override
    public void unpin(Long compId) {
        setPinned(compId, Boolean.FALSE);
    }

    @Override
    public void pin(Long compId) {
        setPinned(compId, Boolean.TRUE);
    }

    private void setPinned(Long compId, Boolean pinned) {
        Compilation comp = compilationDAO.findEntityById(compId);
        comp.setPinned(pinned);
        log.info("Setting Compilation ID:{} as {}pinned", compId, pinned ? "" : "un");
        compilationDAO.save(comp);
    }
}
