package ru.soular.ewm.main.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.main.category.dto.CategoryDto;
import ru.soular.ewm.main.category.dto.NewCategoryDto;
import ru.soular.ewm.main.category.service.AdminCategoryService;
import ru.soular.ewm.main.category.validation.CategoryUpdate;

/**
 * Админский контроллер категорий
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService service;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(service.create(newCategoryDto), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> update(@RequestBody @Validated(CategoryUpdate.class) CategoryDto categoryDto) {
        return new ResponseEntity<>(service.update(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
