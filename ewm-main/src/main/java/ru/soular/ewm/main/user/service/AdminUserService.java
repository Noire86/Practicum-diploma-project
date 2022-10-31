package ru.soular.ewm.main.user.service;

import ru.soular.ewm.main.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс сервиса пользователей
 */
public interface AdminUserService {
    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);

    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

}
