package ru.soular.ewm.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.comment.dao.CommentDAO;
import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.comment.dto.NewCommentDto;
import ru.soular.ewm.main.comment.model.Comment;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.exception.model.ApplicationException;
import ru.soular.ewm.main.user.dao.UserDAO;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.CommentState;
import ru.soular.ewm.main.util.EventState;
import ru.soular.ewm.main.util.mapper.CustomModelMapper;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor

public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final UserDAO userDAO;
    private final EventDAO eventDAO;
    private final CommentDAO commentDAO;
    private final CustomModelMapper mapper;

    @Override
    public CommentDto create(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userDAO.findEntityById(userId);
        Event event = eventDAO.findEntityById(eventId);
        Comment comment = mapper.map(newCommentDto, Comment.class);

        if (!Objects.equals(event.getState(), EventState.PUBLISHED)) {
            throw new ApplicationException("Unable to create a comment for an unpublished event!", HttpStatus.FORBIDDEN);
        }

        comment.setStatus(Objects.equals(event.getCommentModeration(), Boolean.FALSE) ?
                CommentState.APPROVED : CommentState.ON_MODERATION);
        comment.setCommenter(user);
        comment.setEvent(event);

        log.info("Creating new comment by user ID:{} for event ID:{}", userId, eventId);
        return mapper.map(commentDAO.save(comment), CommentDto.class);
    }

    @Override
    public CommentDto update(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentDAO.findEntityById(commentId);

        if (!Objects.equals(comment.getCommenter().getId(), userId)) {
            throw new ApplicationException("You can't update someone else's comment", HttpStatus.FORBIDDEN);
        }

        if (newCommentDto.getText() != null) {
            log.info("Updating comment ID:{} with data={}", commentId, newCommentDto);
            comment.setText(newCommentDto.getText());
            comment.setUpdated(LocalDateTime.now());
        }

        return mapper.map(commentDAO.save(comment), CommentDto.class);
    }

    @Override
    public void delete(Long userId, Long eventId, Long commentId) {
        Comment comment = commentDAO.findEntityById(commentId);

        if (!Objects.equals(comment.getCommenter().getId(), userId)) {
            throw new ApplicationException("You can't delete someone else's comment", HttpStatus.FORBIDDEN);
        }

        log.info("Removing comment ID:{} as user ID:{}", commentId, userId);
        commentDAO.delete(comment);
    }
}
