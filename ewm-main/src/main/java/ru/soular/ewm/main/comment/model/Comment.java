package ru.soular.ewm.main.comment.model;

import lombok.Getter;
import lombok.Setter;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.CommentState;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель комментария
 */
@Entity
@Getter
@Setter
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    private LocalDateTime created = LocalDateTime.now();

    @Column
    private LocalDateTime updated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "commenter_id")
    private User commenter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column
    @Enumerated(EnumType.STRING)
    private CommentState status;
}
