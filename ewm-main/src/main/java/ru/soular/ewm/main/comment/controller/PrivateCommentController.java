package ru.soular.ewm.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.comment.dto.NewCommentDto;
import ru.soular.ewm.main.comment.service.PrivateCommentService;

import javax.validation.Valid;

/**
 * Контроллер для эндпоинтов приватного функционала комментариев
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
public class PrivateCommentController {

    private final PrivateCommentService service;

    @PostMapping
    public ResponseEntity<CommentDto> create(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody NewCommentDto newCommentDto) {

        return new ResponseEntity<>(service.create(userId, eventId, newCommentDto), HttpStatus.OK);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @Valid @RequestBody NewCommentDto newCommentDto) {

        return new ResponseEntity<>(service.update(userId, commentId, newCommentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId) {
        service.delete(userId, commentId);
    }
}
