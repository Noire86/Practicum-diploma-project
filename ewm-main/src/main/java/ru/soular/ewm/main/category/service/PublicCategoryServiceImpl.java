package ru.soular.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.category.dao.CategoryDAO;
import ru.soular.ewm.main.category.dto.CategoryDto;
import ru.soular.ewm.main.category.model.Category;
import ru.soular.ewm.main.util.PageableBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryDAO categoryDAO;
    private final ModelMapper mapper;

    @Override
    public CategoryDto get(Long id) {
        log.info("Getting category with ID:{}", id);
        Category category = categoryDAO.findEntityById(id);
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        log.info("Getting all categories");

        return categoryDAO.findAll(PageableBuilder.build(from, size))
                .getContent()
                .stream()
                .map(cat -> mapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());
    }
}
