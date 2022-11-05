package ru.soular.ewm.stats.dao;

import org.springframework.data.jpa.repository.Query;
import ru.soular.ewm.stats.model.EndpointHit;
import ru.soular.ewm.stats.util.jpa.CustomJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitDAO extends CustomJpaRepository<EndpointHit, Long> {

    /**
     * Получение истории запросов по URI сервиса и времени
     */
    @Query("select hit from EndpointHit as hit " +
            "where hit.uri = ?1 " +
            "and hit.timestamp between ?2 and ?3")
    List<EndpointHit> getEndpointHitsBy(String uri, LocalDateTime start, LocalDateTime end);
}
