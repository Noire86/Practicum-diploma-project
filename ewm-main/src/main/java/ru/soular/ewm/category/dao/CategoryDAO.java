package ru.soular.ewm.category.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.soular.ewm.category.model.Category;

public interface CategoryDAO extends JpaRepository<Category, Long> {

}
