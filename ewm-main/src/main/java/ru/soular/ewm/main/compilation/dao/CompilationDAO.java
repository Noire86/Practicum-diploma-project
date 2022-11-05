package ru.soular.ewm.main.compilation.dao;

import org.springframework.data.domain.Pageable;
import ru.soular.ewm.main.compilation.model.Compilation;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.util.List;

public interface CompilationDAO extends CustomJpaRepository<Compilation, Long> {

    /**
     * Получение подборок по признаку "закреплена"
     */
    List<Compilation> getCompilationsByPinned(Boolean pinned, Pageable pageable);
}
