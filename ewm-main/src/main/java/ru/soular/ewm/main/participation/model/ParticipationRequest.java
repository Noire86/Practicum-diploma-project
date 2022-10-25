package ru.soular.ewm.main.participation.model;

import lombok.Getter;
import lombok.Setter;
import ru.soular.ewm.main.event.model.Event;
import ru.soular.ewm.main.user.model.User;
import ru.soular.ewm.main.util.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "requests", schema = "public")
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}
