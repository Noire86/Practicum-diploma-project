package ru.soular.ewm.main.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.comment.dao.CommentDAO;
import ru.soular.ewm.main.comment.dto.CommentDto;
import ru.soular.ewm.main.comment.model.Comment;
import ru.soular.ewm.main.util.CommentState;
import ru.soular.ewm.main.util.PageableBuilder;
import ru.soular.ewm.main.util.mapper.CustomModelMapper;

import java.util.Collections;
import java.util.List;

/**
 * Имплементация администраторского сервиса
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentServiceImpl implements AdminCommentService {

    private final CommentDAO commentDAO;
    private final CustomModelMapper mapper;

    /**
     * Удаление комментария по айди ивента и комментария
     */
    @Override
    public void delete(Long eventId, Long commentId) {
        if (commentDAO.commentExists(eventId, commentId)) {
            log.info("Removing comment ID:{} from event ID:{}", commentId, eventId);
            commentDAO.deleteById(commentId);
        }
    }

    /**
     * Подтверждение и публикация комментария
     */
    @Override
    public void approve(Long eventId, Long commentId) {
        if (commentDAO.commentExists(eventId, commentId)) {
            log.info("Approving comment ID:{}", commentId);
            changeCommentStatus(commentId, CommentState.APPROVED);
        }
    }


    /**
     * Отклонение комментария
     */
    @Override
    public void reject(Long eventId, Long commentId) {
        if (commentDAO.commentExists(eventId, commentId)) {
            log.info("Rejecting comment ID:{}", commentId);
            changeCommentStatus(commentId, CommentState.REJECTED);
        }
    }

    /**
     * Получение всех комментариев по статусам, поддерживается страничный вывод
     */
    @Override
    public List<CommentDto> getAll(Long eventId, List<CommentState> states, Integer from, Integer size) {
        if (states == null || states.isEmpty()) {
            states = Collections.singletonList(CommentState.APPROVED);
        }

        return mapper.mapList(
                commentDAO.getEventComments(eventId, states, PageableBuilder.build(from, size)),
                CommentDto.class);
    }

    private void changeCommentStatus(Long commentId, CommentState state) {
        Comment comment = commentDAO.findEntityById(commentId);

        if (comment.getStatus().equals(CommentState.ON_MODERATION)) {
            comment.setStatus(state);
            commentDAO.save(comment);
        }
    }
}
