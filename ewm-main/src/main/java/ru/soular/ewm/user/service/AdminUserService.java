package ru.soular.ewm.user.service;

import ru.soular.ewm.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    void delete(Long id);

    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

}
