package ru.soular.ewm.main.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.soular.ewm.main.category.dao.CategoryDAO;
import ru.soular.ewm.main.category.dto.CategoryDto;
import ru.soular.ewm.main.category.dto.NewCategoryDto;
import ru.soular.ewm.main.category.model.Category;
import ru.soular.ewm.main.event.dao.EventDAO;
import ru.soular.ewm.main.exception.model.ApplicationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryDAO categoryDAO;
    private final EventDAO eventDAO;
    private final ModelMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = mapper.map(newCategoryDto, Category.class);
        log.info("Creating new category: {}", newCategoryDto);
        return mapper.map(categoryDAO.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryDAO.findEntityById(categoryDto.getId());

        if (categoryDto.getName() != null && !category.getName().isBlank()) {
            category.setName(categoryDto.getName());
        }

        log.info("Updating category ID: {} with data={}", categoryDto.getId(), categoryDto);
        return mapper.map(categoryDAO.save(category), CategoryDto.class);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing category ID: " + id);

        if (eventDAO.getEventsByCategory_Id(id).size() > 0) {
            throw new ApplicationException("Unable to delete a category with any associated events!", HttpStatus.FORBIDDEN);

        }

        if (categoryDAO.existsById(id)) categoryDAO.deleteById(id);
    }
}
