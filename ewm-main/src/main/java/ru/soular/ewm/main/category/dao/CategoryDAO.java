package ru.soular.ewm.main.category.dao;

import ru.soular.ewm.main.category.model.Category;
import ru.soular.ewm.main.util.jpa.CustomJpaRepository;

public interface CategoryDAO extends CustomJpaRepository<Category, Long> {

}
