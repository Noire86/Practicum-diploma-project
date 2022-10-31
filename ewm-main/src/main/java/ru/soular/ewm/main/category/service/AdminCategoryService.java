package ru.soular.ewm.main.category.service;

import ru.soular.ewm.main.category.dto.CategoryDto;
import ru.soular.ewm.main.category.dto.NewCategoryDto;

/**
 * Интерфейс админского сервиса категорий
 */
public interface AdminCategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);
}
