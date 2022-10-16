package ru.soular.ewm.user.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.soular.ewm.user.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDAO extends JpaRepository<User, Long> {

    List<User> findAllByIdIn(Collection<Long> id, Pageable pageable);
}
