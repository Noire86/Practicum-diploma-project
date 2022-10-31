package ru.soular.ewm.main.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.soular.ewm.main.category.validation.CategoryUpdate;

import javax.validation.constraints.NotNull;

/**
 * ДТО информации о категории
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CategoryDto {

    @NotNull(groups = CategoryUpdate.class)
    private Long id;
    private String name;
}
