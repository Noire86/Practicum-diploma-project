package ru.soular.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.soular.ewm.category.dto.CategoryDto;
import ru.soular.ewm.category.dto.NewCategoryDto;
import ru.soular.ewm.category.service.AdminCategoryService;

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
    public ResponseEntity<CategoryDto> update(@RequestBody @Validated CategoryDto categoryDto) {
        return new ResponseEntity<>(service.update(categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(String.format("Category ID: %d was deleted!", id), HttpStatus.OK);
    }
}
