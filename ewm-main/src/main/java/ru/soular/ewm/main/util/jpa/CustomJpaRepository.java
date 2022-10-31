package ru.soular.ewm.main.util.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;

/**
 * Наследник JPA репозитория, нужен для создания метода и
 * поиска энтитей с выбросом исключения, соответствующего спецификации ТЗ
 */
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    default T findEntityById(ID id) {
        return findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Object with ID:%s was not found", id)));
    }
}
