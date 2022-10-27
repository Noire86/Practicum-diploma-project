package ru.soular.ewm.main.comment.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.soular.ewm.main.comment.model.Comment;
import ru.soular.ewm.main.util.CommentState;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

import java.util.List;

public interface CommentDAO extends CustomJpaRepository<Comment, Long> {

    @Query("select case when count(c) = 1 then true else false end " +
            "from Comment as c where c.event.id = ?1 and c.id = ?2")
    Boolean commentExists(Long eventId, Long commentId);

    @Query("select c from Comment as c where c.event.id = ?1 and c.status in ?2")
    List<Comment> getEventComments(Long eventId, List<CommentState> states, Pageable pageable);

    @Query("select c from Comment as c where c.event.id = ?1 and c.status = 'APPROVED'")
    List<Comment> getApprovedComments(Long eventId);
}
