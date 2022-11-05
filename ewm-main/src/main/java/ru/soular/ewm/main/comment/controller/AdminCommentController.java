package ru.soular.ewm.main.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.comment.service.AdminCommentService;
import ru.soular.ewm.main.util.CommentState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Контроллер для эндпоинтов администраторского функционала комментариев
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events/{eventId}/comments")
public class AdminCommentController {

    private final AdminCommentService service;

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long eventId, @PathVariable Long commentId) {
        service.delete(eventId, commentId);
    }

    @PatchMapping("/{commentId}/approve")
    public void approve(@PathVariable Long eventId, @PathVariable Long commentId) {
        service.approve(eventId, commentId);
    }

    @PatchMapping("/{commentId}/reject")
    public void reject(@PathVariable Long eventId, @PathVariable Long commentId) {
        service.reject(eventId, commentId);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAll(
            @PathVariable Long eventId,
            @RequestParam(required = false) List<CommentState> states,
            @PositiveOrZero @RequestParam(required = false) Integer from,
            @Positive @RequestParam(required = false) Integer size) {

        return new ResponseEntity<>(service.getAll(eventId, states, from, size), HttpStatus.OK);
    }
}
