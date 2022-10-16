package ru.soular.ewm.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import ru.soular.ewm.exception.model.ValidationException;

public class PageableBuilder {

    public static Pageable build(Integer from, Integer size) {
        if (from == null && size == null) return Pageable.unpaged();

        if (from == null || from < 0) {
            throw new ValidationException("Invalid starting pagination parameter!", HttpStatus.BAD_REQUEST);
        }

        if (size == null || size < 1) {
            throw new ValidationException("Invalid page amount pagination parameter!", HttpStatus.BAD_REQUEST);
        }

        return PageRequest.of(from / size, size);
    }
}
