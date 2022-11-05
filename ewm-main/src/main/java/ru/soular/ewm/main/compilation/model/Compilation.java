package ru.soular.ewm.main.compilation.model;

import lombok.Getter;
import lombok.Setter;
import ru.soular.ewm.main.event.model.Event;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * Модель подборки
 */
@Entity
@Getter
@Setter
@Table(name = "compilations", schema = "public")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Boolean pinned;

    @ManyToMany
    @JoinTable(name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(pinned, that.pinned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pinned);
    }
}
