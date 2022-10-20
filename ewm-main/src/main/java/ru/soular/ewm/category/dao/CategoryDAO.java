package ru.soular.ewm.category.dao;

import ru.soular.ewm.category.model.Category;
import ru.soular.ewm.util.jpa.CustomJpaRepository;

public interface CategoryDAO extends CustomJpaRepository<Category, Long> {

}
