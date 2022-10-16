package ru.soular.ewm.category.service;

import ru.soular.ewm.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    CategoryDto get(Long id);

    List<CategoryDto> getAll(Integer from, Integer size);

}
