package ru.soular.ewm.main.comment.service;

import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.comment.dto.NewCommentDto;

public interface PrivateCommentService {

    CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    void delete(Long userId, Long eventId, Long commentId);
}
