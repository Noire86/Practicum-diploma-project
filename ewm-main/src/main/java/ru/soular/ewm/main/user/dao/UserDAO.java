package ru.soular.ewm.main.user.dao;

import org.springframework.data.domain.Pageable;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserDAO extends CustomJpaRepository<User, Long> {

    /**
     * Поиск всех юзеров из переданного списка с айди
     */
    List<User> findAllByIdIn(Collection<Long> id, Pageable pageable);
}
