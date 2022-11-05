package ru.soular.ewm.main.comment.service;

import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.util.CommentState;

import java.util.List;

/**
 * Интерфейс админского сервиса комментариев
 */
public interface AdminCommentService {

    void delete(Long eventId, Long commentId);

    void approve(Long eventId, Long commentId);

    void reject(Long eventId, Long commentId);

    List<CommentDto> getAll(Long eventId, List<CommentState> states, Integer from, Integer size);
}
