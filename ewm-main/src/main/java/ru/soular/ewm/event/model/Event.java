package ru.soular.ewm.event.model;

import lombok.Getter;
import lombok.Setter;
import ru.soular.ewm.category.model.Category;
import ru.soular.ewm.user.model.User;
import ru.soular.ewm.util.EventState;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty
    private String title;

    @Column
    @NotEmpty
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private String description;

    @Column(name = "date_event")
    private LocalDateTime eventDate;

    @Column(name = "created")
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column
    private Boolean paid;

    @PositiveOrZero
    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "location_lat")
    private Double locationLat;

    @Column(name = "location_lon")
    private Double locationLon;
}
