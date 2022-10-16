package ru.soular.ewm.category.service;

import ru.soular.ewm.category.dto.CategoryDto;
import ru.soular.ewm.category.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);
}
