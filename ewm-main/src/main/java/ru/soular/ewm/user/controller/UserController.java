package ru.soular.ewm.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.user.dto.UserDto;
import ru.soular.ewm.user.service.UserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Validated UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping
    public List<UserDto> getAll(
            @RequestParam(required = false) List<Long> ids,
            @Positive @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @PatchMapping("/{id}")
    public UserDto update(
            @PathVariable Long id,
            @RequestBody @Validated UserDto userDto) {
        return userService.update(id, userDto);
    }
}
