package ru.soular.ewm.compilation.dao;

import org.springframework.data.domain.Pageable;
import ru.soular.ewm.compilation.model.Compilation;
import ru.soular.ewm.util.jpa.CustomJpaRepository;

import java.util.List;

public interface CompilationDAO extends CustomJpaRepository<Compilation, Long> {

    List<Compilation> getCompilationsByPinned(Boolean pinned, Pageable pageable);
}
