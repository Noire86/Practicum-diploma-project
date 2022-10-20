package ru.soular.ewm.user.dao;

import org.springframework.data.domain.Pageable;
import ru.soular.ewm.user.model.User;
import ru.soular.ewm.util.jpa.CustomJpaRepository;

import java.util.Collection;
import java.util.List;

public interface UserDAO extends CustomJpaRepository<User, Long> {

    List<User> findAllByIdIn(Collection<Long> id, Pageable pageable);
}
