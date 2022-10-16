package ru.soular.ewm.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.user.dto.UserDto;
import ru.soular.ewm.user.service.AdminUserService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController  {
    private final AdminUserService service;

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Validated UserDto userDto) {
        return new ResponseEntity<>(service.create(userDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @RequestBody @Validated UserDto userDto) {
        return new ResponseEntity<>(service.update(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(
            @RequestParam(required = false) List<Long> ids,
            @Positive @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(service.getAll(ids, from, size), HttpStatus.OK);
    }
}
