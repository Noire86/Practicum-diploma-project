package ru.soular.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.soular.ewm.category.dao.CategoryDAO;
import ru.soular.ewm.category.dto.CategoryDto;
import ru.soular.ewm.category.dto.NewCategoryDto;
import ru.soular.ewm.category.model.Category;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private final CategoryDAO categoryDAO;
    private final ModelMapper mapper;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = mapper.map(newCategoryDto, Category.class);
        log.info("Creating new category: {}", newCategoryDto);
        return mapper.map(categoryDAO.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = categoryDAO.getReferenceById(categoryDto.getId());

        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }

        log.info("Updating category ID: {} with data={}", categoryDto.getId(), categoryDto);
        return mapper.map(categoryDAO.save(category), CategoryDto.class);
    }

    @Override
    public void delete(Long id) {
        log.info("Removing category ID: " + id);
        Category cat = categoryDAO.getReferenceById(id);
        categoryDAO.delete(cat);
    }
}
